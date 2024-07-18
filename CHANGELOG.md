### 2.1.0-alpha.2

Following versions after this one are subject to dramatic changes. This is the version for those who just want a working
release largely faithful to the old 2022 release.

- Re-worked `config.yml` (yes, this means you have to delete your old one)
- Re-added commands
- Minor changes no one will notice

**What's next?**

Future patches before completed 2.1.0 release:

- Improve command parsing and tab completion

Next major release (3.0.0):

- Completely re-work the listeners
- Switch to TOML because I like TOML

### 2.1.0-alpha.1 (Rewrite)

- Rewrote the entire project (WIP)
- Somehow reduced the size of the plugin JAR by **99%**
- Switched to Gradle because I don't like Maven

It's been two years now. And let's face it, the codebase was... uh... in an interesting state.  
I'm not sure how it even got to be this way to be honest; I mean, all you have to do is look at the main class to get a
good idea of what I had to deal with:

```java

@Override
public void onEnable() {
    init();
}

private void init() {
    regEvents();
    // ...
}

private void regEvents() {
    regEvent(new PlayerDamageListener(this));
    regEvent(new PlayerDeathListener(this));
    regEvent(new DragonDeathListener(this));
}

private void regEvent(Listener event) {
    getServer().getPluginManager().registerEvents(event, this);
}
```

Holy abstraction. And what the hell is _this_ in PlayerDamageListener?!

```java

// A switch-case that goes through every f*cking cause of death in the game, literally
    switch(cause){
        case BLOCK_EXPLOSION ->

broadcast(GetConfigString("dmsg_BlockExplosion").

replaceAll("%PLAYER%",damaged.getDisplayName()));
        case CONTACT ->

broadcast(GetConfigString("dmsg_Contact").

replaceAll("%PLAYER%",damaged.getDisplayName()));
        case CRAMMING ->

broadcast(GetConfigString("dmsg_Cramming").

replaceAll("%PLAYER%",damaged.getDisplayName()));
        case DRAGON_BREATH ->

broadcast(GetConfigString("dmsg_DragonBreath").

replaceAll("%PLAYER%",damaged.getDisplayName()));
        case DROWNING ->

broadcast(GetConfigString("dmsg_Drowning").

replaceAll("%PLAYER%",damaged.getDisplayName()));
        case ENTITY_ATTACK ->

broadcast(GetConfigString("dmsg_EntityAttack").

replaceAll("%PLAYER%",damaged.getDisplayName()));
        case ENTITY_EXPLOSION ->

broadcast(GetConfigString("dmsg_EntityExplosion").

replaceAll("%PLAYER%",damaged.getDisplayName()));
        case FALL ->

broadcast(GetConfigString("dmsg_Fall").

replaceAll("%PLAYER%",damaged.getDisplayName()));

// Rinse and repeat, 16 times...

default ->

broadcast(GetConfigString("AnnounceDeathText").

replaceAll("%PLAYER%",damaged.getDisplayName()));
        }

```

So, I hope this gives you a grander appreciation for the amount of pain I went through trying to rewrite everything from
scratch since basically nothing was salvageable, and maybe you'll forgive me for not having everything done in this
release.

**So, what's missing?**

Well, there are no commands right now, and the `config.yml` is still just as awful as it was to begin with. Half of the
key-value pairs in there don't do anything anymore either. I also haven't really had a chance to try out everything,
which is why this is an "alpha" release. But, hey, at least we don't need to worry about Issue #1 from **two years ago
**, because you need Java 21 to run a 1.21 Minecraft server anyway!

**Where do we even go from here...**

The next step from here is obvious: Add the commands back in and fix the `config.yml`. When this will happen? Who knows.
Unlike the young and naïve courtega/arrivals from 2022, I'm an adult with an adult job and adult responsibilities, so I
can't make any promises on a timeline. But I'll try to get something out in the next week or so.

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