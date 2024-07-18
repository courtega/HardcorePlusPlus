# Hardcore++ by courtega

_Fork of griimnak's HardcorePlus_

Lose hearts when you die. Turn on your friends to regain them.

[![stability-experimental](https://img.shields.io/badge/stability-dubious_at_best-red.svg)](https://github.com/emersion/stability-badges#stable)

# Usage

Hardcore++ is automatically enabled on server start.

Users with the `hardcoreplusplus.admin` permission may use the following commands:

- Disable Hardcore++ with `/hcpp disable`
- Enable Hardcore++ with `/hcpp enable`
- Set a player's max health with `/hcpp setmax <player> <int>` (a full heart is 2 HP)

Users with the `hardcoreplusplus.ban_exempt` permission are excluded from being banned.

# Changelog

_View entire changelog history in `/CHANGELOG.md`_

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