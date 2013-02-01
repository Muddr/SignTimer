# v2.0.0 - Works with CB 1.3.2-R0.2 - CB 1.4.7-R1.0
- ** Major Added:** Support for multiple timers.
- Added: Ability to rename timers.
    - /st rename <oldName> <newName>
    - Permission signtimer.rename - Defaults to OP
- Added: Ability to enable/disable timers.
    - /st enable <timerName> or /st disable <timerName> 
    - Permission signtimer.enabledisable - Defaults to OP
- Added: Ability to delete timers.
    - NOTE: Will clear times and remove signs for that timer.
    - /st delete <timerName> 
    - Permission signtimer.delete - Defaults to OP
- Added: Config Changes:
    - Option to cancel timer if player dies/disconnects. Default true.
    - Option to set the number of times to display with the top command. Default 10.
- Added: Command to list all timers. /st list
- **Major Change:** Database Structure Changed
    - **Database will be backed up and converted on first run**
- **Major Change:** Sign Format Changed
    - Line1 [SignTimer] 
    - Line2 <TimerName>
    - Line3 Start or Stop
- Changed: Reworked /st command to only show commands the player has permissions to use.
    - Also shows non command based permissions.
- Changed: Top10 command is now just top. /st top <timer name>
- Changed: All commands now require a timer name(except list and name is optional with mytime). /st command <timername>


# v1.1.0 - Works with CB 1.3.2-R0.2 - CB 1.4.6-R0.2+
- Added: command and permission to clear all times. defaults to OP.
- Added: permission to allow players to remove sign timers instead of just OPs. defaults to OP.
- Added: permission to allow players to create sign timers. defaults to OP.
- Added: Compact time format and config setting to disable it.
- Added: MCStats integrations. See Server Owners Section at https://mcstats.org/learn-more/ to opt-out
 
# v1.0.0
- Initial release.