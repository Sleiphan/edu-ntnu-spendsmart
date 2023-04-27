# Welcome to SpendSmart 🤖

 SpendSmart is a newly launched desktop application for personal banking and budgeting purposes 💸. It offers a wide range of features including creating budgets and providing budget suggestions, as well as managing and updating bank account details 💳.

## Installation Guide 🖥️
To run the SpendSmart software, simply run the .jar file. 

**To ensure proper functioning of SpendSmart, it is recommended that you create a new and empty folder to place the .jar file in, as the application generates files that are necessary for its operation.**

To run SpendSmart, the only requirement is that Java version 17 or higher must be installed on your system.



### To verify whether Java is installed on your system and determine its version, you can perform the following steps:
#### **Windows:** 

Open the Command Prompt and run the following command: `java -version` .

Verify that Java is installed with version 17+.

#### **macOS:**

Open the Terminal and run the following command: `java -version`.

Verify that Java is installed with version 17+.

## Usage Instructions 

For detailed usage instructions, refer to the 
[User Manual in the project's GitLab.](https://gitlab.stud.idi.ntnu.no/idatt1002_2023_group14/idatt1002_2023_group14/-/wikis/Home/System/User-manual)

## Known Issues ⛔️

Since the program uses the BigDecimal library, the program is quite relient on the set language of the host computer. Some test will fail if the host computer has a diffrent default language set than english. This should not affect the program itself, only the tests under TestUser.
