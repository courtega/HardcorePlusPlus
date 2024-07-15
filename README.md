# Hardcore++ by courtega

_Fork of griimnak's HardcorePlus_

Lose hearts when you die. Turn on your friends to regain them.

[![stability-experimental](https://img.shields.io/badge/stability-dubious_at_best-red.svg)](https://github.com/emersion/stability-badges#stable)

# Changelog

### 2.1.0-alpha.1 (Rewrite)

- Rewrote the entire project (WIP)
- Somehow reduced the size of the plugin JAR by **99%**
- Switched to Gradle because I don't like Maven

It's been two years now. And let's face it, the codebase was... uh... in an interesting state.  
I'm not sure how it even got to be this way to be honest; I mean, all you have to do is look at the main class to get a good idea of what I had to deal with:

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
    switch (cause) {
        case BLOCK_EXPLOSION ->
                broadcast(GetConfigString("dmsg_BlockExplosion").replaceAll("%PLAYER%", damaged.getDisplayName()));
        case CONTACT ->
                broadcast(GetConfigString("dmsg_Contact").replaceAll("%PLAYER%", damaged.getDisplayName()));
        case CRAMMING ->
                broadcast(GetConfigString("dmsg_Cramming").replaceAll("%PLAYER%", damaged.getDisplayName()));
        case DRAGON_BREATH ->
                broadcast(GetConfigString("dmsg_DragonBreath").replaceAll("%PLAYER%", damaged.getDisplayName()));
        case DROWNING ->
                broadcast(GetConfigString("dmsg_Drowning").replaceAll("%PLAYER%", damaged.getDisplayName()));
        case ENTITY_ATTACK ->
                broadcast(GetConfigString("dmsg_EntityAttack").replaceAll("%PLAYER%", damaged.getDisplayName()));
        case ENTITY_EXPLOSION ->
                broadcast(GetConfigString("dmsg_EntityExplosion").replaceAll("%PLAYER%", damaged.getDisplayName()));
        case FALL ->
                broadcast(GetConfigString("dmsg_Fall").replaceAll("%PLAYER%", damaged.getDisplayName()));
        
        // Rinse and repeat, 16 times...

        default ->
                broadcast(GetConfigString("AnnounceDeathText").replaceAll("%PLAYER%", damaged.getDisplayName()));
    }

```

So, I hope this gives you a grander appreciation for the amount of pain I went through trying to rewrite everything from scratch since basically nothing was salvageable, and maybe you'll forgive me for not having everything done in this release.

**So, what's missing?**

Well, there are no commands right now, and the `config.yml` is still just as awful as it was to begin with. Half of the key-value pairs in there don't do anything anymore either. I also haven't really had a chance to try out everything, which is why this is an "alpha" release. But, hey, at least we don't need to worry about Issue #1 from **two years ago**, because you need Java 21 to run a 1.21 Minecraft server anyway!

**Where do we even go from here...**

The next step from here is obvious: Add the commands back in and fix the `config.yml`. When this will happen? Who knows. Unlike the young and naïve courtega/arrivals from 2022, I'm an adult with an adult job and adult responsibilities, so I can't make any promises on a timeline. But I'll try to get something out in the next week or so.