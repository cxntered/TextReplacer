<div align="center">

# `TextReplacer`

A mod using OneConfig to visually replace any text.

![Showcase](images/showcase.png)

</div>

## Variables

The mod supports variables in both the text you want to match and the replacement text. These variables are automatically replaced with their current values. <br />
For example, you can use variables to change the color of your username or replace the server IP displayed in the scoreboard. To use a variable, prefix its name with the pilcrow sign (`¶`).

**Available variables:** `username`, `serverIp`, `serverDomain`, `hypixelScoreboardIp` <br />
If you have an idea for a new variable, [create an issue!](https://github.com/cxntered/TextReplacer/issues)

## Match Filters

Each replacement can ignore certain characters when matching text. These filters apply to both the target and the text being matched.

- **Formatting codes** – ignores `§` formatting codes, so `§cHello` matches `Hello`.
- **Invalid characters** – ignores invalid characters that do not render, commonly found in scoreboards like on Hypixel.

You may have to use these filters if your replacement is not working as expected, like when trying to match scoreboard lines.
