package plugins

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter
import javafx.scene.Node
import model.SampleAttachment
import mu.KotlinLogging
import tornadofx.*
import ui.events.MessageAddedEvent
import ui.plugin.MessagePlugin
import java.awt.Desktop
import java.io.BufferedReader
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter

val logger = KotlinLogging.logger { }

fun main() {
    openGoogleEarth(SampleAttachment().filename!!)
}

class GoogleEarthPlugin : MessagePlugin() {

    override fun onMessageReceived(event: MessageAddedEvent) {
        if (event.message.attachment.isImage) {
            runLater {
                addButton(event.messageNode, event.message.attachment.filename!!)
            }
        }
    }

    private fun addButton(node: Node, filename: String) {
        node.button {
            graphic = imageview("earth.png") {
                fitWidth = 24.0
                fitHeight = 24.0
            }
            action {
                logger.info(filename)
            }
        }
    }
}

fun openGoogleEarth(filename: String) {
    logger.info("Using Google Earth to open ${filename}")

    val exif = parseExif(filename)

    logger.info(exif.toString())
    val file = createKmz(exif, filename)
    Desktop.getDesktop().open(file)
}

fun parseExif(filename: String): Map<String, Double> {
    val p = Runtime.getRuntime().exec(arrayOf("/usr/local/bin/exiftool", "-n", filename))
    val lines = p.inputStream.bufferedReader().use(BufferedReader::readLines)

    val m = mutableMapOf<String, Double>()

    m["lat"] = lines.parseTag("GPS Latitude")
    m["lon"] = lines.parseTag("GPS Longitude")
    m["alt"] = lines.parseTag("Relative Altitude")

    m["width"] = lines.parseTag("Image Width")
    m["height"] = lines.parseTag("Image Height")

    m["roll"] = lines.parseTag("Flight Roll Degree") + lines.parseTag("Gimbal Roll Degree")
    m["yaw"] = lines.parseTag("Flight Yaw Degree")
    m["pitch"] = lines.parseTag("Flight Pitch Degree") + lines.parseTag("Gimbal Pitch Degree")

    m["fov"] = lines.parseTag("Field Of View")

    return m
}

fun List<String>.parseTag(tag: String): Double {
    for (line in this) {
        if (line.matches(Regex("$tag[\\s]*:.*"))) {
            return line.split(":")[1].toDouble()
        }
    }
    return 0.0
}

fun createKmz(exif: Map<String, Double>, image: String): File {
    val tmpdir = createTempDir("imessage")
    logger.info("Created $tmpdir")

    val imageFile = File(image)
    val copied = imageFile.copyTo(File("$tmpdir/files/${imageFile.name}"))
    logger.info("Copied $copied")

    val kmlFile = File("$tmpdir/doc.kml")
    writeDoc(exif, kmlFile, copied)
    logger.info("Wrote $kmlFile")

    val kmzFile = createTempFile("imessage", ".kmz")
    packToZip(tmpdir.path, kmzFile.path)

    logger.info("Created $kmzFile")

    return kmzFile
}

fun writeDoc(exif: Map<String, Double>, kmlFile: File, imageFile: File) {
    val writer =
        IndentingXMLStreamWriter(XMLOutputFactory.newFactory().createXMLStreamWriter(kmlFile.outputStream(), "UTF-8"))
    writer.document {
        element("kml") {
            attribute("xmlns", "http://www.opengis.net/kml/2.2")
            attribute("xmlns:gx", "http://www.google.com/kml/ext/2.2")
            attribute("xmlns:kml", "http://www.opengis.net/kml/2.2")
            attribute("xmlns:atom", "http://www.w3.org/2005/Atom")
            element("PhotoOverlay") {
                element("name", imageFile.name)
                element("Camera") {
                    element("longitude", exif.getValue("lon").toString())
                    element("latitude", exif.getValue("lat").toString())
                    element("altitude", exif.getValue("alt").toString())
                    element("heading", exif.getValue("yaw").toString())
                    element("tilt", (90 + exif.getValue("pitch")).toString())
                    element("roll", exif.getValue("roll").toString())
                    element("gx:altitudeMode", "relativeToSeaFloor")
                }
                element("Icon") {
                    element("href", "files/${imageFile.name}")
                }
                element("ViewVolume") {
                    element("leftFov", "-${exif.getValue("fov") / 2}")
                    element("rightFov", "${exif.getValue("fov") / 2}")
                    element("bottomFov", "-${20 * exif.getValue("width") / exif.getValue("height")}")
                    element("topFov", "${20 * exif.getValue("width") / exif.getValue("height")}")
                    element("near", "${20 * exif.getValue("width") / exif.getValue("height")}")
                }
                element("Point") {
                    element(
                        "Coordinates",
                        "${exif.getValue("lon")}," +
                                "${exif.getValue("lat")}," +
                                "${exif.getValue("alt")}"
                    )
                }
            }
        }
    }

    writer.flush()
}

fun packToZip(sourceDirPath: String, zipFilePath: String) {
    File(zipFilePath).let { if (it.exists()) it.delete() }

    val zipFile = Files.createFile(Paths.get(zipFilePath))

    ZipOutputStream(Files.newOutputStream(zipFile)).use { stream ->
        val sourceDir = Paths.get(sourceDirPath)
        Files.walk(sourceDir).filter { path -> !Files.isDirectory(path) }.forEach { path ->
            val zipEntry = ZipEntry(path.toString().substring(sourceDir.toString().length + 1))

            stream.putNextEntry(zipEntry)
            stream.write(Files.readAllBytes(path))
            stream.closeEntry()
        }
    }
}


fun XMLStreamWriter.document(init: XMLStreamWriter.() -> Unit): XMLStreamWriter {
    this.writeStartDocument()
    this.init()
    this.writeEndDocument()
    return this
}

fun XMLStreamWriter.element(name: String, init: XMLStreamWriter.() -> Unit): XMLStreamWriter {
    this.writeStartElement(name)
    this.init()
    this.writeEndElement()
    return this
}

fun XMLStreamWriter.element(name: String, content: String) {
    element(name) {
        writeCharacters(content)
    }
}

fun XMLStreamWriter.attribute(name: String, value: String) = writeAttribute(name, value)