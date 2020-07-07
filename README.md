# aArmorStandCmds
A spigot plugin that allows attaching commands to armor stands

Admin permission: `aarmorstandcmds.admin`

## Commands:
`/astand add <command>` - Add a command to an armor stand, the commands will be executed by the player

`/astand remove <command>` - Remove a command from an armor stand

`/astand list` - List armor stand commands

## Usage Example:
`/astand add msg %player% welcome`

This will message the player that right clicks the armor stand "welcome"

## Default Config:
```
# allow handling for /server command, will send the target to a server instead of executing the command
bungee-support: true
# disable interactions with armorstands for everyone
protect-armorstands: false
data: []
```
