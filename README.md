Start the bot:
```sh
java -jar FourSeasonsGamingBot-0.1.0_23-all.jar \
	--token [BOT TOKEN] \
	--url [UPDATE JSON URL]
```
The bot token can be obtained from https://discordapp.com/developers/applications/


Example update JSON URL body
```json
{
  "Updates": [
    {
      "Discord_Post_Channel_Id": "",
      "Message": ""
    },
    {
      "Discord_Post_Channel_Id": "",
      "Message": ""
    }
  ]
}
```
