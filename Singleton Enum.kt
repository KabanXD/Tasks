// Задача 1: Класс базы данных как Singleton
class Database private constructor() {

    init {
        println("Подключение к базе данных создано.")
    }

    companion object {
        private var instance: Database? = null

        fun getInstance(): Database {
            if (instance == null) {
                instance = Database()
            }
            return instance!!
        }
    }
}

// Задача 2: Логирование в системе как Singleton
class Logger private constructor() {

    private val logs = mutableListOf<String>()

    fun addLog(message: String) {
        logs.add(message)
    }

    fun showLogs() {
        if (logs.isEmpty()) {
            println("Нет логов.")
        } else {
            logs.forEach { println(it) }
        }
    }

    companion object {
        private var instance: Logger? = null

        fun getInstance(): Logger {
            if (instance == null) {
                instance = Logger()
            }
            return instance!!
        }
    }
}

// Задача 3: Реализация статусов заказа
enum class OrderStatus {
    NEW, IN_PROGRESS, DELIVERED, CANCELLED
}

class Order(var status: OrderStatus) {

    fun changeStatus(newStatus: OrderStatus) {
        if (status == OrderStatus.DELIVERED) {
            println("Невозможно отменить доставленный заказ.")
            return
        }

        if (status == OrderStatus.CANCELLED) {
            println("Невозможно изменить статус отмененного заказа.")
            return
        }

        status = newStatus
        println("Статус заказа изменен на $status")
    }

    fun showStatus() {
        println("Текущий статус заказа: $status")
    }
}

// Задача 4: Сезоны года
enum class Season {
    WINTER, SPRING, SUMMER, AUTUMN
}

fun getSeasonName(season: Season): String {
    return when (season) {
        Season.WINTER -> "Зима"
        Season.SPRING -> "Весна"
        Season.SUMMER -> "Лето"
        Season.AUTUMN -> "Осень"
    }
}

fun main() {
    // Задача 1: Проверка Singleton для базы данных
    val db1 = Database.getInstance()
    val db2 = Database.getInstance()
    println("db1 и db2 ссылаются на один и тот же объект: ${db1 === db2}")
    println("------------")
    // Задача 2: Проверка логирования
    val logger1 = Logger.getInstance()
    logger1.addLog("Ошибка при подключении к серверу.")
    logger1.addLog("Пользователь вошел в систему.")
    val logger2 = Logger.getInstance()
    logger2.addLog("Новый лог.")
    println("Логи из logger1:")
    logger1.showLogs()
    println("Логи из logger2:")
    logger2.showLogs()
    println("logger1 и logger2 ссылаются на один и тот же объект: ${logger1 === logger2}")
    println("------------")

    // Задача 3: Проверка статусов заказа
    val order = Order(OrderStatus.NEW)
    order.showStatus()
    order.changeStatus(OrderStatus.IN_PROGRESS)
    order.showStatus()
    order.changeStatus(OrderStatus.DELIVERED)
    order.showStatus()
    order.changeStatus(OrderStatus.CANCELLED)
    order.showStatus()

    val anotherOrder = Order(OrderStatus.NEW)
    anotherOrder.changeStatus(OrderStatus.CANCELLED)
    anotherOrder.showStatus()
    println("------------")
    // Задача 4: Проверка сезонов года
    val season = Season.WINTER
    println("Сезон: ${getSeasonName(season)}")
}
