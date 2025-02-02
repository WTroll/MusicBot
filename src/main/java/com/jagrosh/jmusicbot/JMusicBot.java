/*
 * Copyright 2016 John Grosh (jagrosh).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jagrosh.jmusicbot;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.PingCommand;
import com.jagrosh.jmusicbot.commands.admin.PrefixCmd;
import com.jagrosh.jmusicbot.commands.admin.SetdjCmd;
import com.jagrosh.jmusicbot.commands.admin.SettcCmd;
import com.jagrosh.jmusicbot.commands.admin.SetvcCmd;
import com.jagrosh.jmusicbot.commands.dj.ForceRemoveCmd;
import com.jagrosh.jmusicbot.commands.dj.ForceskipCmd;
import com.jagrosh.jmusicbot.commands.dj.MoveTrackCmd;
import com.jagrosh.jmusicbot.commands.dj.PauseCmd;
import com.jagrosh.jmusicbot.commands.dj.PlaynextCmd;
import com.jagrosh.jmusicbot.commands.dj.RepeatCmd;
import com.jagrosh.jmusicbot.commands.dj.SkiptoCmd;
import com.jagrosh.jmusicbot.commands.dj.StopCmd;
import com.jagrosh.jmusicbot.commands.dj.VolumeCmd;
import com.jagrosh.jmusicbot.commands.general.SettingsCmd;
import com.jagrosh.jmusicbot.commands.music.LyricsCmd;
import com.jagrosh.jmusicbot.commands.music.NowplayingCmd;
import com.jagrosh.jmusicbot.commands.music.PlayCmd;
import com.jagrosh.jmusicbot.commands.music.PlaylistsCmd;
import com.jagrosh.jmusicbot.commands.music.QueueCmd;
import com.jagrosh.jmusicbot.commands.music.RemoveCmd;
import com.jagrosh.jmusicbot.commands.music.SCSearchCmd;
import com.jagrosh.jmusicbot.commands.music.SearchCmd;
import com.jagrosh.jmusicbot.commands.music.ShuffleCmd;
import com.jagrosh.jmusicbot.commands.music.SkipCmd;
import com.jagrosh.jmusicbot.commands.owner.AutoplaylistCmd;
import com.jagrosh.jmusicbot.commands.owner.DebugCmd;
import com.jagrosh.jmusicbot.commands.owner.EvalCmd;
import com.jagrosh.jmusicbot.commands.owner.PlaylistCmd;
import com.jagrosh.jmusicbot.commands.owner.SetavatarCmd;
import com.jagrosh.jmusicbot.commands.owner.SetgameCmd;
import com.jagrosh.jmusicbot.commands.owner.SetnameCmd;
import com.jagrosh.jmusicbot.commands.owner.SetstatusCmd;
import com.jagrosh.jmusicbot.commands.owner.ShutdownCmd;
import com.jagrosh.jmusicbot.settings.SettingsManager;
import java.util.Arrays;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author John Grosh (jagrosh) */
public class JMusicBot {
  public static final String PLAY_EMOJI = "\u25B6"; // ▶
  public static final String PAUSE_EMOJI = "\u23F8"; // ⏸
  public static final String STOP_EMOJI = "\u23F9"; // ⏹
  public static final Permission[] RECOMMENDED_PERMS = {
      Permission.MESSAGE_READ,
      Permission.MESSAGE_WRITE,
      Permission.MESSAGE_HISTORY,
      Permission.MESSAGE_ADD_REACTION,
      Permission.MESSAGE_EMBED_LINKS,
      Permission.MESSAGE_ATTACH_FILES,
      Permission.MESSAGE_MANAGE,
      Permission.MESSAGE_EXT_EMOJI,
      Permission.MANAGE_CHANNEL,
      Permission.VOICE_CONNECT,
      Permission.VOICE_SPEAK,
      Permission.NICKNAME_CHANGE
  };
  public static final GatewayIntent[] INTENTS = {
      GatewayIntent.DIRECT_MESSAGES,
      GatewayIntent.GUILD_MESSAGES,
      GatewayIntent.GUILD_MESSAGE_REACTIONS,
      GatewayIntent.GUILD_VOICE_STATES
  };
  /** @param args the command line arguments */
  public static void main(String[] args) {
    // startup log
    Logger log = LoggerFactory.getLogger("Startup");

    // load config
    BotConfig config = new BotConfig();
    config.load();
    if (!config.isValid()) return;

    // set up the listener
    EventWaiter waiter = new EventWaiter();
    SettingsManager settings = new SettingsManager();
    Bot bot = new Bot(waiter, config, settings);

    // set up the command client
    CommandClientBuilder cb =
        new CommandClientBuilder()
            .setPrefix(config.getPrefix())
            .setAlternativePrefix(config.getAltPrefix())
            .setOwnerId(Long.toString(config.getOwnerId()))
            .setEmojis(config.getSuccess(), config.getWarning(), config.getError())
            .setHelpWord(config.getHelp())
            .setLinkedCacheSize(200)
            .setGuildSettingsManager(settings)
            .addCommands(
                new PingCommand(),
                new SettingsCmd(bot),
                new LyricsCmd(bot),
                new NowplayingCmd(bot),
                new PlayCmd(bot),
                new PlaylistsCmd(bot),
                new QueueCmd(bot),
                new RemoveCmd(bot),
                new SearchCmd(bot),
                new SCSearchCmd(bot),
                new ShuffleCmd(bot),
                new SkipCmd(bot),
                new ForceRemoveCmd(bot),
                new ForceskipCmd(bot),
                new MoveTrackCmd(bot),
                new PauseCmd(bot),
                new PlaynextCmd(bot),
                new RepeatCmd(bot),
                new SkiptoCmd(bot),
                new StopCmd(bot),
                new VolumeCmd(bot),
                new PrefixCmd(bot),
                new SetdjCmd(bot),
                new SettcCmd(bot),
                new SetvcCmd(bot),
                new AutoplaylistCmd(bot),
                new DebugCmd(bot),
                new PlaylistCmd(bot),
                new SetavatarCmd(bot),
                new SetgameCmd(bot),
                new SetnameCmd(bot),
                new SetstatusCmd(bot),
                new ShutdownCmd(bot));
    if (config.useEval()) {
      cb.addCommand(new EvalCmd(bot));
    }
    boolean nogame = false;
    if (config.getStatus() != OnlineStatus.UNKNOWN) {
      cb.setStatus(config.getStatus());
    }
    if (config.getGame() == null) {
      cb.useDefaultGame();
    } else if (config.getGame().getName().equalsIgnoreCase("none")) {
      cb.setActivity(null);
      nogame = true;
    } else {
      cb.setActivity(config.getGame());
    }

    log.info("Loaded config from " + config.getConfigLocation());

    // attempt to log in and start
    try {
      JDA jda =
          JDABuilder.create(config.getToken(), Arrays.asList(INTENTS))
              .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
              .disableCache(
                  CacheFlag.ACTIVITY,
                  CacheFlag.CLIENT_STATUS,
                  CacheFlag.EMOTE,
                  CacheFlag.ONLINE_STATUS)
              .setActivity(nogame ? null : Activity.playing("loading..."))
              .setStatus(
                  config.getStatus() == OnlineStatus.INVISIBLE
                      || config.getStatus() == OnlineStatus.OFFLINE
                      ? OnlineStatus.INVISIBLE
                      : OnlineStatus.DO_NOT_DISTURB)
              .addEventListeners(cb.build(), waiter, new Listener(bot))
              .setBulkDeleteSplittingEnabled(true)
              .build();
      bot.setJDA(jda);
      // Registers hook to shut down the bot when JVM receives SIGINT
      Runnable cleanup = bot::shutdown;
      Runtime.getRuntime().addShutdownHook(new Thread(cleanup));
    } catch (LoginException ex) {
      String errorMessage = """
          Please make sure you are\040
          editing the correct config.txt file, and that you have used the\040
          correct token (not the 'secret'!)
          Config Location:\040""" + config.getConfigLocation();
      log.error(errorMessage);
      System.exit(1);
    } catch (IllegalArgumentException ex) {
      String errorMessage =
          """
          Some aspect of the configuration is invalid: 
          """ + ex + "\nConfig Location: " + config.getConfigLocation();
      log.error(errorMessage, ex);
      System.exit(1);
    }
  }
}
