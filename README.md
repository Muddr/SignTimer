# SignTimer

A stopwatch plugin using signs to start/stop the clock. Great for timed competitions (mazes, speed runs, etc).

## Quick Info
- Current Version: 2.0.0
- CB Version: CB 1.4.7-R1.0
- Changelog: <https://github.com/Muddr/SignTimer/blob/master/CHANGELOG.md>
- Download: <http://dev.bukkit.org/server-mods/signtimer/files/3-v2-0-0/>

## Features

- Multiple timer support
- Start and stop timers
- Top command to list for fastest times
- See your own personal best times
- See other player's best times

## Todo List

- Look into announcing times to the player when timer is on.
- Trying to implement more feature requests.

# Updating to v2.0.0
- When the server is first started the config will be updated and the database will be backed up and converted to the new format.
- After the server has started the original start and stop signs will need to be right clicked to update them to the new format.
- the original signs will use the timername `default`.  Use /st rename default <newName> to change it.
- /st will list only commands that player has permission to use.

### Default config.yml:
	settings:
	  broadcast_times: false
	  include_days: false
	  include_milli: true
	  compact_time: true
	  top_amount: 10

	timer:
	  stop_on_death: true
	  stop_on_quit: true
	
	messages:
	  broadcast_message: "%PLAYERNAME% finished %TIMERNAME% in %TIME%"
	  player_message: "Your time for %TIMERNAME% was %TIME%"
	
	DB:
	  driver: 'org.sqlite.JDBC'
	  url: 'jdbc:sqlite:{DIR}{NAME}.db'
	  username: 'root'
	  password: ''
	  isolation: 'SERIALIZABLE'
	  logging: false
	  rebuild: false
	  version: 0

### Explanation
All settings will be shown in node syntax for ease of documentation, e.g. `category.someitem` will refer to the `someitem` setting in the `category` group.

`settings.broadcast_times`
- Whether the time will be shown to all players or just you. `false` = only the player sees their time, `true` = all players see the time.
- Default: `false`

`settings.include_days`
- Whether the time will output the amount of days, if false hours will go over 24 hours.
- Default: `false`

`settings.include_milli`
- Whether the time will be shown down to the millisecond. This only works if `settings.compact_time` is also true.
- Default: `true`

`settings.compact_time`
- Whether the time will be shown a compact format `01d:01h:01s:0001ms` instead of `1 day 1 hour 1 minute 1 seconds`. Setting this to true will also make the top10 command cleaner looking.
- Default: `true`

`settings.top_amount` - **NEW for v2.0.0**
- Sets the amount to times to list with the `top` command.
- Default: 10

`timer.stop_on_death` - **NEW for v2.0.0**
- Whether or not the timers should stop if a player dies.
- Default: true

`timer.stop_on_quit` - **NEW for v2.0.0**
- Whether or not the timers should stop if a player disconnects or quits.
- Default: true

`messages.broadcast_message` - **Changed formats for v2.0.0**
- Message that is broadcast to all players showing the time. Use tokens to personalize the message. *Note: this message only shows when `settings.broadcast_times` is `true`*
	- Tokens:
		- `%PLAYERNAME%`: name of the player that finished the timer
		- `%TIME%`: time that it took for the player to finish the timer
		- `%TIMERNAME%: the name of the timer
- Default: `"%PLAYERNAME% finished %TIMERNAME% in %TIME%"`

`messages.player_message` - **Changed formats for v2.0.0**
- Message that is broadcast to the player that finished the timer. Use tokens to personalize the message. *Note: this message only shows when `settings.broadcast_times` is `false`*
	- Tokens:
		- `%TIME%`: time that it took for the player to finish the timer
		- `%TIMERNAME%: the name of the timer
- Default: `"Your time for %TIMERNAME% was %TIME%"`

#### DB related settings
It is highly recommended that this section of the config be left as-is. **Only attempt changing this section if you know what you are doing!**

**Issues regarding database settings will not be answered.** They will be closed as we will most likely not be able to answer them. This section is provided as a courtesy for advanced users if they need it.

## Commands
- **All commands changed for v2.0.0 to add <timerName>**
- `/st clear <timerName>`
    Clears all times for <timerName>.
- `/st delete <timerName>`
    Deletes <timerName> and removes all times/signs.
- `/st enable <timerName>`
    Enables <timername>.
- `/st disable <timerName>`
    Disables <timername>.
- `/st list`
    Lists all timers. color coded for enabled/disabled.
- `/st mytime <timerName>`
    Lists all your times. <timerName> is optional.
- `/st rank <playerName> <timerName>`
    Displays the rank for <playerName>'s time for <timerName>.
- `/st rename <oldName> <newName>`
    Renames timer from <oldName> to <newName>. Will update signs also.
- `/st top <timerName>`
    Lists the top <config_Setting_Amount> times for <timerName>    

## Permissions
- `signtimer.removesigns`
	description: Allows player to remove sign timers.
	default: op
- `signtimer.createsigns`
	description: Allows player to create sign timers.
	default: op
- `signtimer.cleartimes`
	description: Allows player to clear all saved times for a timer.
	default: op
- `signtimer.delete` - **NEW for v2.0.0**
	description: Allows player to delete a timer.
	default: op
- `signtimer.enabledisable` - **NEW for v2.0.0**
	description: Allows player to enable/disable timers.
	default: op
- `signtimer.list` - **NEW for v2.0.0**
	description: Allows player to list timers.
	default: op
- `signtimer.rename` - **NEW for v2.0.0**
	description: Allows player to rename timers.
	default: op

## Creating Sign Timers - **MAJOR CHANGE for v2.0.0**
To create a sign timer, place a sign with the following information:
- **Line 1:** `[signtimer]`
- **Line 2:** `timername`
- **Line 3:** `start` OR `stop`

*Note: lines 1 and 3 are case insensitive. Ex: `Start`, `sTaRt` as well as any variant will all work.*
**Line 2 must be the same for both signs and commands involving timernames.**

**Example:**
![Sign timer example] (https://dl.dropbox.com/u/18835236/SignTimer/signexample.png)

#### Using the timers
Right-click on the "start" timer to start, and right-click on the "stop" timer to stop. It is *that* simple.