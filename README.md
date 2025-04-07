
# Библиотека деревьев поиска (Kotlin)
Данный проект реализует следующие деревья поиска на языке Kotlin:

- Бинарное дерево поиска

- AVL-дерево

- Красно-черное дерево


## Функциональность
Для всех деревьев поддерживается:

- вставка элемента
- удаление элемента
- поиск вершины
- возможность итерирования по ключам и значениям
- поиск элемента с максимальным ключом
- поиск элемента с минимальным ключом

## Структура проекта

```
project-root/
├── gradle/
│   └── wrapper/
├── src/
│   ├── main/
│   │   └── kotlin/         # Основные исходные файлы
│   │       ├── AVLTree.kt                       # AVL-дерево
│   │       ├── AbstractBinarySearchTree.kt      # Абстрактный класс деревьев
│   │       ├── BinarySearchTree.kt              # Реализация бинарного дерева
│   │       ├── BinaryTree.kt                    # Интерфейс бинарного дерева
│   │       ├── Nodes.kt                         # Классы для вершин деревьев
│   │       └── RedBlackTree.kt                  # Красно-черное дерево
│   └── test/               # Тесты
│       └── kotlin/
│           ├── AVLTreeTest.kt                   # Тесты для AVL-дерева
│           ├── BinarySearchTreeTest.kt          # Тесты для бинарного дерева
│           ├── RBTTest.kt                       # Тесты для красно-черного дерева
│           └── TestableBinarySearchTree.kt      # Класс с методами для тестирования бинарного дерева
├── build.gradle.kts
└── settings.gradle.kts

```


## Локальный запуск

Клонирование репозитория

```bash
  git clone https://github.com/spbu-coding-2024/trees-trees-team-10.git
```

Переход в директорию проекта

```bash
  cd trees-trees-team-10
```

Сборка библиотеки

```bash
  ./gradlew jar
```
После этого в папке build/libs появится jar-aрхив, копируем путь до него и подключаем в зависимости в build.gradle.kts вашего проекта, как показано в примере:
```kotlin
dependencies {
    implementation(files("/home/directory/trees-trees-team-10/build/libs/BinaryTrees-1.0-SNAPSHOT.jar"))
}
```




## Запуск тестов

```bash
  ./gradlew test
```


## Использование

Бинарное дерево поиска и AVL-дерево можно создать следующим образом:

```kotlin
fun main() {
    //создание бинарного дерева поиска
    val bst = BinarySearchTree<Int, String>()
    
    //создание AVL-дерева
    val avl = AVLTree<Int, String>()
}
```

Для создания красно-черного дерева необходимо использовать одну из функций:
1) emptyRedBlackTree() - возвращает пустое красно-черное дерево
2) redBlackTreeOf() - позволяет заполнить дерево ключами и значениями

```kotlin
//пример использования функции redBlackTreeOf()

fun main() {
    val rbt = redBlackTreeOf(
        10 to "ten",
        5 to "five",
        2 to "two",
        3 to "three",
        7 to "seven"
    )
}
```
