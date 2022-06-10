# CAB302 2022 Maze Generator
A maze generator is implemented in Java, from the user stories
defined in the [assignment specification](Assignment%20Specification.pdf).

## Installation Instruction:
Ensure JDK 17 is installed on the machine trying to run the .jar file. All other dependencies are bundled together.
To run the .jar file, either use the command line, in the same directory:
`-java -jar MazeGen.jar`
Or double click the MazeGen.jar.
A database configurations (`db.props`) and database file (`cab302.db`) are created on run. 

## Requirements
Due to the author's having multiple other commitments, this project was not finished in due time (10/6/2022).
The following table lists the completion status of the software, with 19 out of 24 requirements satisfied.

| Number | Title                 | Description                                        | Importance | Status    |
|---|---|---|---|---|
| 1      | Maze Creation         | Generate an empty maze                             | HIGH | Done      |
| 2      | Maze Algorithm        | Generate a maze using different algorithms         | HIGH | Done      |
| 3      | Maze Edit             | A generated maze is editable                       | HIGH | Done      |
| 4      | Solvable Maze         | A generated maze is solvable                       | HIGH| Done      |
| 5      | Solution              | Toggle solution display on or off                  | MEDIUM |Done      |
| 6      | Entrances             | Arrows pointing where the maze starts and finishes | LOW | Cancelled |
| 7      | Images Import         | Can put an image in the maze                       | MEDIUM |Done      |
|8 | Images Blocking       | Images are walled off                              | MEDIUM | Cancelled |
|9| Images Resize         | Images can be resized                              | LOW | Done      |
|10| Images Delete         | Images can be deleted from the maze once imported  | LOW | Done      |
|11| Generate around image | Algorithm can take images into consideration       | MEDIUM |Cancelled |
|12| Images Import         | Import images from computer with picker            | MEDIUM |Done      |
|13| Export Maze           | Select location to export maze as an image         | HIGH |Done      |
|14| Size limit            | Maximum limit is 100x100                           | MEDIUM |Done      |
|15| Maze Details          | Add maze name, author name with a maze             | MEDIUM |Done      |
|16| Maze database | Store maze details, along with created and modified dates in database | HIGH | Done      |
|17| Browse mazes | Browse saved mazes from database and load them | HIGH | Done      |
|18| Delete maze | Delete a saved maze from the database | MEDIUM | Done      |
|19| Browse sort | Browser sorts the mazes based on name, author and dates | MEDIUM | Done      |
|20| Maze metrics | Reports percentage of reachable mazes, and dead-ends | MEDIUM | Cancelled |
|21| Mass export | Browse through mazes and export them at once | MEDIUM | Cancelled |
|22| Export resolution | Higher resolution for bigger mazes | MEDIUM | Done |
|23| Database properties | `db.props` file to store database information | HIGH | Done |
|24| Create database when needed | Create database on startup if not present | HIGH | Done |

