# AI Chess Engine Using MinMax
A customizable chess game which allows you to play human vs human, computer vs human and computer vs computer. Chess being a zero-sum game, the computer AI is implemented through the MinMax algorithm.

I have implemented my own categories for calculating the value at each stage. Thus, the MinMax algorithm will look at 
future moves (4 moves if depth is 4) and will judge the future moves based on the categories thereby selecting the optimal
move. Despite the large number of possible moves during each chess position, the AI engine has been implemented in such 
a way that it analyzes the position and plays the optimal move in almost no time.

<img width="500" alt="Screenshot 2024-02-22 at 11 49 18 PM" src="https://github.com/Jai0212/AI-Chess-Engine/assets/86296165/816b77cb-29f5-4a74-b951-650edec91a9e">
<img width="500" alt="Screenshot 2024-02-22 at 11 41 35 PM" src="https://github.com/Jai0212/AI-Chess-Engine/assets/86296165/dc72623f-3679-4939-bce9-151f3c883056">

## Features
* AI supported chess engine with customizable depth
* Customizable board colours and legal move indicating dots
* Ability to flip the chess board
* Interactive GUI which shows the captured pieces and the moves in chess notation
* Customizable chess engine by altering MinMax properties
* Readable and simple implementation using OOPs
* All chess moves implemented including en passant, pawn promotion, stalemate and castling

<img width="330" alt="Screenshot 2024-02-22 at 11 42 58 PM" src="https://github.com/Jai0212/AI-Chess-Engine/assets/86296165/94b6bece-6df4-4b90-9f18-41f8af0b09c5">
<img width="330" alt="Screenshot 2024-02-22 at 11 32 46 PM" src="https://github.com/Jai0212/AI-Chess-Engine/assets/86296165/1c3ab166-9a61-4363-af99-ecd824c06141">
<img width="330" alt="Screenshot 2024-02-22 at 11 47 09 PM" src="https://github.com/Jai0212/AI-Chess-Engine/assets/86296165/de4f4a7a-747b-47c7-9c2c-2c810ce1be8f">

## Technical Aspects, Usage and Acknowledgement
* Moves are calculated using the MinMax algorithm with a customizable depth to check for the best move possible
* Code written in Java on IntelliJ
* The GUI was implemented through JFrame in the package javax.swing (preinstalled)

To use, just download the code and run the 'Chess' class. No need to install anything else. Can be run on any normal 
device that supports java for human vs human and upto depth 4 for the AI. Beyond that it will use more time 
and resources.  

I worked on this project alone and will not be actively working on the project anymore (I will be creating other 
related projects). However, I would love any suggestions/feedback/collaborative requests.


## Author and Date
by Jai Joshi  
23rd February, 2024
