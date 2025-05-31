# AdminPanel Application

## Overview

AdminPanel is a Java application designed to provide administrative functionalities within a software system. It offers features such as accessing a general database and managing user accounts.

## Features

- *General Database Access*: Users can access a general database to perform administrative tasks.
- *User Management*: Administrators can manage user accounts, including adding, editing, and deleting user information.

## Prerequisites

Before running the application, ensure you have the following installed:

- Java Development Kit (JDK) 8 or higher
- An Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse

## Installation

1. Clone or download the repository to your local machine.
2. Open the project in your preferred IDE.

## Usage

1. Run the Main.java file.
2. Upon launching, the application presents an AdminPanel interface with options to access the general database or manage user accounts.
3. Click on the desired functionality to proceed.
4. To log out, click the "Logout" button at the bottom of the interface.

## Localization

The application supports localization in different languages. The default language is determined by the system locale. Users can change the language using the LanguagePanel.

# General Database Package

## Overview

The GeneralDatabase package contains classes for managing a general database of books. It provides functionality for adding, editing, and removing books from the database.

## AdminDatabase Class

The AdminDatabase class represents the user interface for managing the general database. It allows administrators to view, add, edit, and remove books from the database. The class includes features such as sorting, filtering, and searching for books.

### Features

- *View Books*: Displays a table of books with details such as title, author, rating, and review.
- *Add Book*: Allows administrators to add a new book to the database.
- *Edit Book*: Enables administrators to edit the details of an existing book.
- *Remove Book*: Allows administrators to remove a book from the database.
- *Search*: Provides a search functionality to find books by title or author.
- *Localization*: Supports localization in different languages.

## Book Class

The Book class represents a single book in the database. It contains attributes such as title, author, rating, and review.

### Attributes

- *Titles*: A list of titles associated with the book.
- *Author*: The author of the book.

## Panel Class

The Panel class is an alternative interface for managing the general database. It provides similar functionality to the AdminDatabase class but also includes features for managing personal book collections.

### Features

- *Switch Database*: Allows users to switch between the general database and their personal book collection.
- *Add to Personal Collection*: Enables users to add books from the general database to their personal collection.
- *Localization*: Supports localization for different languages.

## Usage

1. *AdminDatabase*: Run the AdminDatabase class to manage the general database.
2. *Panel*: Run the Panel class for an alternative interface with personal collection management.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse

# GUI Package

The gui package contains classes for creating graphical user interfaces (GUIs) in Java Swing. These classes are used to build interactive and visually appealing applications.

## EditBookWindow Class

The EditBookWindow class represents a dialog window for editing book details. It allows users to modify the title, author, rating, review, status, time spent, start date, and end date of a book. Users can save the changes or cancel the editing process.

### Features

- *Edit Book Details*: Users can modify various attributes of a book, including its title, author, rating, review, status, and dates.
- *Save Changes*: Users can save the changes made to the book details.
- *Cancel Editing*: Users can cancel the editing process without saving changes.
- *Date and Time Handling*: The window automatically sets the current date and time for the start date and end date fields.

## Usage

1. *EditBookWindow*: Instantiate the EditBookWindow class with the book details to be edited. Display the window to allow users to make changes.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse

# Internationalization Package

The internationalization package contains classes for implementing language localization in Java Swing applications. These classes enable users to select their preferred language and customize the application's interface accordingly.

## LanguagePanel Class

The LanguagePanel class represents a panel for selecting the application's language. It provides buttons to choose between English and Azerbaijani languages. When a language is selected, the application's interface is updated accordingly.

### Features

- *Language Selection*: Users can choose between English and Azerbaijani languages.
- *Interface Localization*: The application's interface is localized based on the selected language.

## Usage

1. *LanguagePanel*: Add the LanguagePanel to your Swing application to enable language selection functionality.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse

# Login Package

The login package contains classes for user authentication and registration in a Java Swing application. It includes functionality for logging in, registering new users, and managing user credentials.

## LogInPage Class

The LogInPage class represents the login page of the application. Users can enter their credentials to log in or navigate to the registration page to create a new account. Features of this class include:

- *Login*: Users can log in with their username and password.
- *Registration*: Users can register for a new account if they don't have one.
- *Localization*: The login page supports multiple languages using the LanguagePanel class for language selection.

## RegistrationPage Class

The RegistrationPage class represents the registration page of the application. Users can create a new account by providing a username and password. Features of this class include:

- *Account Creation*: Users can create a new account by providing a valid username and password.
- *Password Validation*: Passwords must meet certain criteria (e.g., length, character types) for security.
- *Credential Management*: User credentials are stored securely and managed using CSV files.

## LanguagePanel Class

The LanguagePanel class provides language selection functionality for the application. Users can choose between English and Azerbaijani languages, and the interface updates accordingly.

## Usage

1. *LogInPage*: Use this class to implement the login functionality in your Swing application.
2. *RegistrationPage*: Use this class to implement user registration functionality.
3. *LanguagePanel*: Use this class to enable language selection in your application.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse

# Login Package

The login package contains classes for user authentication and registration in a Java Swing application. It includes functionality for logging in, registering new users, and managing user credentials.

## LogInPage Class

The LogInPage class represents the login page of the application. Users can enter their credentials to log in or navigate to the registration page to create a new account. Features of this class include:

- *Login*: Users can log in with their username and password.
- *Registration*: Users can register for a new account if they don't have one.
- *Localization*: The login page supports multiple languages using the LanguagePanel class for language selection.

## RegistrationPage Class

The RegistrationPage class represents the registration page of the application. Users can create a new account by providing a username and password. Features of this class include:

- *Account Creation*: Users can create a new account by providing a valid username and password.
- *Password Validation*: Passwords must meet certain criteria (e.g., length, character types) for security.
- *Credential Management*: User credentials are stored securely and managed using CSV files.

## LanguagePanel Class

The LanguagePanel class provides language selection functionality for the application. Users can choose between English and Azerbaijani languages, and the interface updates accordingly.

## Login Exceptions

The loginexceptions package contains custom exception classes used in the login package:

- *EmptyUsernamePasswordException*: Thrown when the username or password field is empty during registration.
- *InvalidCredentials*: Thrown when the provided credentials are invalid during login.
- *InvalidPasswordException*: Thrown when the provided password does not meet the required criteria during registration.
- *InvalidUsernameException*: Thrown when the provided username does not meet the required criteria during registration.

## Usage

1. *LogInPage*: Use this class to implement the login functionality in your Swing application.
2. *RegistrationPage*: Use this class to implement user registration functionality.
3. *LanguagePanel*: Use this class to enable language selection in your application.
4. *Login Exceptions*: Handle these exceptions appropriately in your application's login and registration processes.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse

# Personal Database Package

The Personal Database package provides functionality for managing personal book collections in a Java Swing application. It includes features for adding, editing, and removing books, as well as saving and loading data from CSV files.

## PersonalBook Class

The PersonalBook class represents a personal book entry in the collection. It contains information such as the book title, author, user rating, review, status, time spent, and start/end dates for tracking reading progress.

## PersonalBookPanel Class

The PersonalBookPanel class implements a graphical user interface for managing personal book collections. It displays a table with columns for book details and provides options for searching, editing, removing, and saving changes to a CSV file.

### Features:

- *Table Display*: Displays personal book collection data in a table with customizable columns.
- *Search*: Allows users to search for specific books by title or author.
- *Edit*: Enables users to edit book details directly within the table.
- *Remove*: Allows users to remove books from the collection.
- *Save*: Saves changes to a CSV file for persistent storage.
- *Localization*: Supports multiple languages using the LanguagePanel class for language selection.

## Usage

1. *PersonalBook*: Use this class to represent individual books in a personal collection.
2. *PersonalBookPanel*: Use this class to implement a graphical user interface for managing personal book collections.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse

# Sorting and Filtering Package

The Sorting and Filtering package provides functionality for sorting table columns in a Java Swing application. It allows users to click on column headers to toggle between ascending, descending, and unsorted order for sorting data in tables.

## TableSorting Class

The TableSorting class enables sorting functionality for JTables by listening for mouse clicks on column headers. It maintains the sorting state for each column and updates the table accordingly.

### Features:

- *Column Sorting*: Allows users to click on column headers to toggle between ascending, descending, and unsorted order for sorting table data.
- *Sort Order Management*: Maintains the sorting state for each column and updates the table based on the current sort order.
- *Integration with DefaultTableModel and TableRowSorter*: Works seamlessly with Swing's DefaultTableModel and TableRowSorter to sort table data.

## Usage

1. *TableSorting*: Use this class to enable sorting functionality for JTables in your Java Swing application.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse

# User Management Package

The User Management package provides functionality for managing user accounts in a Java Swing application. It allows administrators to view, edit, and delete user accounts from the system.

## UserManagementPanel Class

The UserManagementPanel class represents a JPanel that displays a table of user accounts and provides options to edit or delete them. Administrators can also navigate to other sections of the application, such as the General Library or logout from the system.

### Features:

- *User Account Management*: Allows administrators to view, edit, and delete user accounts from the system.
- *Integration with Internationalization*: Supports localization using language resource bundles for multilingual user interfaces.
- *Interactive Navigation*: Provides a JComboBox for administrators to select options such as editing users, accessing the General Library, or logging out.

## Usage

1. *UserManagementPanel*: Use this class to integrate user account management functionality into your Java Swing application for administrators.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- Integrated Development Environment (IDE) such as IntelliJ IDEA or Eclipse