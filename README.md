## Serversided Freecam Plugin
This plugin adds a command (/c) and (/s) which allow you to enter or exit freecam respectively.
You can change the max distance that can be travelled (radius) in the config by changing:

```
radius: 150
```
to whatever number you want.

There is also a guardian NPC function. If you have Citizens + Sentinel installed and the respective config enabled

```
npc:
  enabled: true
```

when someone goes into freecam a clone of them with all of their loot will be made. If that clone dies then all of their loot, armor, etc will drop and they will be killed. This makes it so that people can't abuse it in e.g. PvP.

Keep in mind, the guardian NPC function will be automatically disabled if you are missing the required plugins.
