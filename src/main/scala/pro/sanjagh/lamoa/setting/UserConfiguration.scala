package pro.sanjagh.lamoa.setting

import com.typesafe.config.{Config, ConfigFactory}
import pro.sanjagh.lamoa.model.{Proxy, ProxyType}
import pro.sanjagh.lamoa.util.BrushConsole

import java.nio.file.{Files, Path, Paths, StandardOpenOption}

/**
 * This Config file related to user manual configuration such language and proxy
 */
object UserConfiguration extends Configuration {

  private val config: Config = {
    val confPath = Path.of(
      s"${System.getProperty("user.home")}",
      ".config",
      "lamoa",
      "config"
    )

    if (!Files.exists(confPath))
      checkConfigFileExist()

    ConfigFactory.parseFile(confPath.toFile)
  }

  lazy val language: String =
    Option(config.getString("language")).getOrElse("English")

  lazy val connection: Proxy = Option(Proxy(config.getConfig("proxy")))
    .getOrElse(Proxy(ProxyType.Direct, "", 0))

  private lazy val getConfigFileString: String = Option(
    AppConfiguration.config.getString("userConfigFileContent")
  ).get

  /**
   * check config directory and file exist. unless generate a new one
   */
  def checkConfigFileExist(): Unit = {
    val path: Path =
      Paths.get(s"${System.getProperty("user.home")}", ".config", "lamoa")
    val configFile = path.resolve("config")

    if (!Files.isDirectory(path))
      Files.createDirectory(path)

    if (!Files.exists(configFile)) {
      BrushConsole.printInfoMessage(
        s"""Config file created under $configFile. you can configure app through this file.
\t- In the propose of build or reset config file, remove specified file."""
      )
      Files.write(
        configFile,
        getConfigFileString.getBytes,
        StandardOpenOption.CREATE
      )
    }
  }
}
