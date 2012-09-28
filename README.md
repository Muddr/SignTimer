# SignTimer

A stopwatch plugin using signs to start/stop the clock. Great for timed competitions (mazes, speed runs, etc).

## Quick Info
- Current Version: 1.0.0
- CB Version: CB 1.3.2-R0.2
- Changelog: <https://github.com/Muddr/SignTimer/blob/master/CHANGELOG.md>
- Mirror Download: <https://github.com/downloads/Muddr/SignTimer/signtimer.jar>

## Features

- Start and stop timers
- Top 10 list for fastest times
- See your own personal best time
- See other player's best time

## Todo List

- clean up the columns for the /st top10 command.
- Look into announcing times to the player when timer is on.
- add permissions to be able to remove signs instead of having to be Op.
- add option to display 00d:00h:00m:00s:00ms time format.

### Default config.yml:
	settings:
	  broadcast_times: false

	messages:
	  broadcast_message: "%PLAYERNAME% finished in %TIME%"
	  player_message: "Your time was %TIME%"

	DB:
	  driver: 'org.sqlite.JDBC'
	  url: 'jdbc:sqlite:{DIR}{NAME}.db'
	  username: 'root'
	  password: ''
	  isolation: 'SERIALIZABLE'
	  logging: false
	  rebuild: false

### Explanation
All settings will be shown in node syntax for ease of documentation, e.g. `category.someitem` will refer to the `someitem` setting in the `category` group.

`settings.broadcast_times`
- Whether the time will be shown to all players or just you. `false` = only the player sees their time, `true` = all players see the time.
- Default: `false`

`messages.broadcast_message`
- Message that is broadcast to all players showing the time. Use tokens to personalize the message. *Note: this message only shows when `settings.broadcast_times` is `true`*
	- Tokens:
		- `%PLAYERNAME%`: name of the player that finished the timer
		- `%TIME%`: time that it took for the player to finish the timer
- Default: `"%PLAYERNAME% finished in %TIME%"`

`messages.player_message`
- Message that is broadcast to the player that finished the timer. Use tokens to personalize the message. *Note: this message only shows when `settings.broadcast_times` is `false`*
	- Tokens:
		- `%TIME%`: time that it took for the player to finish the timer
- Default: `"Your time was %TIME%"`

#### DB related settings
It is highly recommended that this section of the config be left as-is. **Only attempt changing this section if you know what you are doing!**

**Issues regarding database settings will not be answered.** They will be closed as we will most likely not be able to answer them. This section is provided as a courtesy for advanced users if they need it.

## Commands
- `/st mytime`
	Displays your personal best time.
- `/st rank <name>`
	Displays the rank and best time for `<name>` where `<name>` is the name of the player you want.
- `/st top10`
	Lists the top 10 fastest times.

## Creating Sign Timers
To create a sign timer, place a sign with the following information:
- **Line 1:** `[signtimer]`
- **Line 2:** `start` OR `stop`

*Note: both lines are case insensitive. Ex: `Start`, `sTaRt` as well as any variant will all work.*

**Example:**
![Sign timer example] (http://i.imgur.com/Sgdml.png)

#### Using the timers
Right-click on the "start" timer to start, and right-click on the "stop" timer to stop. It is *that* simple.

#### Removing the signs
Only OPs can remove Sign Timers.