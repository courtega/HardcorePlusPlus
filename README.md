<h1 align="center">Hardcore++

by arrivals
</h1>


[![stability-experimental](https://img.shields.io/badge/stability-stable-green.svg)](https://github.com/emersion/stability-badges#stable)
<b>Latest release build:</b> <a href="https://github.com/griimnak/Minecraft-HardPlus/releases">here</a>

<b>
Fork of griimnak's HardcorePlus

Lose hearts when you die, gain hearts when you kill other player's
</b>

View spigot page for more information on usage.

<br/>
<br/>

<h3><i>Hardcore++ Changelog</i></h3>

<b>[Aug 8 2022]</b>

Initial Release (2.0.0)

Note: DELETE HARDCOREPLUS AND ITS CONFIG!

- Updated to 1.19 API
- Rewrote command system
    - Added tab completion
    - Allows for selectors (i.e. @a) (implements CommandUtils by ZombieStriker)
- Implemented life stealing (enabled by default)
- Cosmetic changes
- Respawning is now randomized within a radius to prevent mob spawn camping
- Rewrote enable/disable system
- Disabled hardcore requirement (was unnecessary)
- Many other changes

<h3><i>HardcorePlus Changelog</i></h3>

<b>[Aug 10 2019]</b>

Thanks NotToBlame for helping find the following bugs.

NOTE: Delete your old config before using the new version.

- Fixed a suffucation bug when dying in a dimension other than the overworld.
- Fixed an issue with /hardcoreplus command crashing when plugin is disabled.
- Fixed a bug with /hardcoreplus setmax command.
- Custom state management, since disabling the plugin was buggy.
- Maxhealth is reset on final death, incase an admin wishes to unban the player.

<b>[July 13 2019]</b>

HardcorePlus 1.0.0 - Major release

Thank you to OnyxianSoul for suggesting the rework of hardcoreCheck() method.

- Plugin completely re written
- Servers must now have hardcore enabled in server.properties.
- Fixed bug with extra "invisible" health after max health is updated.
- Source cleaned and using more oop, compartmentalized
- Spigot api updated to spigot-api:1.14.1

- Added option to temp ban on respawn/fake death.
- Added option to permaban on real death/perma death.
- Added option to announce respawn/fake death to server.
- Added option to keep inventory respawn/fake death.
- Added option to keep experience on respawn/fake death.

- Added hardcoreplus.ban_exempt permission to allow players to ignore banondeath
- Added /hardcoreplus disable command to disable plugin ingame.
- Added stat tracking, so far only "total-perm-dead-players" is avaiable.

- Removed weakness on respawn/fake death.

<b>[June 14 2019]</b>

HardcorePlus 0.1.8 corrects some minor annoyances and finally introduces the configuration system.

Thank you to @LucasLogical for finding the following bugs.

- Fixed bug with shields not working properly
- Fixed bug with blood effect showing through shields

- New command: /hardcoreplus reload - reloads the config.yml

- Config system complete
- Config options for toggling: bloodEffect, respawnSound, respawnEffect, respawnWeakness
- Config option for toggling max hp restore by ender dragon kill
- Config options for all ingame texts
- Config option for enforcing hardcore mode upon server.

<b>[May 18 2019]</b>

- increased weakness after death to 3 minutes 30 sec
- hardcoreplus commands are accessible from server console now
- if hardcore is not set in server.properties console will be notified

- fixed bug with setmax command always settings to 10 hearts
- fixed bug with fire and potion effects persisting after death
- fixed bug with setmax command allowing use of negative numbers
- fixed bug with setmax command allowing use of invalid numbers

<b>[May 15 2019]</b>

- Users can now restore their max hp by killing the ender dragon.
- Added blood effect on hit
- Corrected creeper instakill bug
- Added sound effect to death
- Added admin permission
- Enderdragon kill event
- Started admin commands

<b>[May 13 2019]</b>

- Changed health lost from 1 to 2 hearts.
- Changed DeathEvent to EntityDamagedEvent
- Death is no longer announced.
- "Dream" effect created.
