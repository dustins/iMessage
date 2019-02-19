package db

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.StatementContext
import org.jetbrains.exposed.sql.statements.StatementInterceptor
import org.jetbrains.exposed.sql.statements.expandArgs
import java.sql.PreparedStatement

class TimingInterceptor : StatementInterceptor {
    var startTime: Long = 0
    private val logger = KotlinLogging.logger{}

    override fun beforeExecution(transaction: Transaction, context: StatementContext) {
        logger.debug("Preparing ${context.expandArgs(transaction)}")
        startTime = System.nanoTime()
    }

    override fun afterExecution(
        transaction: Transaction,
        contexts: List<StatementContext>,
        executedStatement: PreparedStatement
    ) {
        logger.debug("Number of contexts: ${contexts.size}")
        logger.info("${(System.nanoTime() - startTime)/1_000_000}ms " +
                "to execute ${contexts.first().expandArgs(transaction)}")
    }
}