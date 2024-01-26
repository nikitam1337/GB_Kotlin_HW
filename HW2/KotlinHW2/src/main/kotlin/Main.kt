//## Урок 2. Практика по классам в Kotlin
//За основу берём код решения домашнего задания из предыдущего семинара и дорабатываем его.
//
//— Создайте иерархию sealed классов, которые представляют собой команды. В корне иерархии интерфейс Command.
//— В каждом классе иерархии должна быть функция isValid(): Boolean, которая возвращает true, если команда введена с корректными аргументами.
// Проверку телефона и email нужно перенести в эту функцию.
//— Напишите функцию readCommand(): Command, которая читает команду из текстового ввода, распознаёт её и возвращает один из классов-наследников Command,
// соответствующий введённой команде.
//— Создайте data класс Person, который представляет собой запись о человеке. Этот класс должен содержать поля:
//name – имя человека
//phone – номер телефона
//email – адрес электронной почты
//— Добавьте новую команду show, которая выводит последнее значение, введённой с помощью команды add.
// Для этого значение должно быть сохранено в переменную типа Person. Если на момент выполнения команды show не было ничего введено,
// нужно вывести на экран сообщение “Not initialized”.
//— Функция main должна выглядеть следующем образом. Для каждой команды от пользователя:
// Читаем команду с помощью функции readCommand
// Выводим на экран получившийся экземпляр Command
// Если isValid для команды возвращает false, выводим help. Если true, обрабатываем команду внутри when.

import java.util.regex.Pattern

// Интерфейс для команд
sealed interface Command {
    fun isValid(): Boolean
}

// Классы команд
data class AddPhoneCommand(val name: String, val phone: String) : Command {
    override fun isValid() = phone.matches(Regex("\\+?\\d+"))
    override fun toString(): String {
        return "Вызвана команда добавления с указанием телефона"
    }
}

data class AddEmailCommand(val name: String, val email: String) : Command {
    override fun isValid() = email.matches(Regex("\\w+@\\w+\\.\\w+"))
    override fun toString(): String {
        return "Вызвана команда добавления с указанием почты"
    }
}

object ExitCommand : Command {
    override fun isValid() = true
}

object HelpCommand : Command {
    override fun isValid() = true
}

object ShowCommand : Command {
    override fun isValid() = true
}

// Класс Person
data class Person(var name: String, var phone: String? = null, var email: String? = null)

fun readCommand(input: String): Command {
    val parts = input.split(" ")
    // Распознавание команды
    return when (parts[0]) {
        "add" -> {
            if (parts.size == 4) {
                when (parts[2]) {
                    "phone" -> AddPhoneCommand(parts[1], parts[3])
                    "email" -> AddEmailCommand(parts[1], parts[3])
                    else -> HelpCommand
                }
            } else {
                return HelpCommand
            }
        }

        "exit" -> ExitCommand
        "help" -> HelpCommand
        "show" -> ShowCommand
        else -> HelpCommand
    }
}

fun main() {
    println(
        "Введите одну из команд:\n" +
                "1. exit\n" +
                "2. help\n" +
                "3. add <Имя> phone <Номер телефона>\n" +
                "4. add <Имя> email <Адрес электронной почты>\n" +
                "5. show"
    )
    var person: Person? = null
    while (true) {
        val command = readCommand(readLine()!!)
        println(command)
        if (command.isValid()) {
            when (command) {
                is AddPhoneCommand -> {
                    person = Person(command.name, phone = command.phone)
                    println("Добавлено: ${person!!.name}, телефон: ${person!!.phone}")
                }

                is AddEmailCommand -> {
                    person = Person(command.name, email = command.email)
                    println("Добавлено: ${person!!.name}, email: ${person!!.email}")
                }

                is ShowCommand -> {
                    if (person == null) {
                        println("Not initialized")
                    } else {
                        println("Последняя запись: $person")
                    }
                }

                is HelpCommand -> {
                    println(
                        "Список доступных команд:\n" +
                                "1. exit\n" +
                                "2. help\n" +
                                "3. add <Имя> phone <Номер телефона>\n" +
                                "4. add <Имя> email <Адрес электронной почты>\n" +
                                "5. show"
                    )
                }

                is ExitCommand -> return
                else -> println("Неизвестная команда")
            }
        } else {
            println("Неверный формат команды, попробуйте еще раз")
        }
    }
}