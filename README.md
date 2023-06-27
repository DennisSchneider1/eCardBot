# eCardBot
This is a discord bot that allows its users to play the game of e-card.
The used language for user interaction is german.

For information on how the game is played, see:
- [http://ecard.zertukis.com/](http://ecard.zertukis.com/)
(This page shows the rules and has a little demo of the game.)

Its worth considering that the game isn't supposed to be played against a bot since bluffing is the main mechanic of the game.
As a result this bot only supports a player versus player mode.

---
- The bot is coded in Java and uses the now outdated JDA dicord API.
- Private information like the player cards in hand are transmited over direct message,
- while the game is started and managed on a server this bot has access to.
- User identifiers and gained currency is stored locally in a file. 

Here is a overview of the commands the bot supports:

![Ecard](https://github.com/DennisSchneider1/eCardBot/assets/49718250/641c8770-42fe-49e8-a010-837223de4610)

Here is a Example for the Banana Lottery Game:

![ecard-banana-game](https://github.com/DennisSchneider1/eCardBot/assets/49718250/dab76a9a-17e0-4004-b4b8-941e8d396c9c)
