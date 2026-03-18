@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val theme = SoundStageThemeManager.currentTheme
    
    // Scoping ViewModels at the NavGraph level is the "Pro" way to keep music playing
    val playerVm: PlayerViewModel = viewModel()

    Scaffold(
        bottomBar = {
            // Sleek, minimal nav bar
            NavigationBar(containerColor = theme.background, contentColor = theme.primary) {
                listOf("player", "library", "eq", "settings").forEach { route ->
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate(route) },
                        icon = { Text(route.take(1).uppercase(), color = theme.primary, fontFamily = FontFamily.Monospace) },
                        label = { Text(route.uppercase(), fontSize = 8.sp) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController, 
            startDestination = "player", 
            modifier = Modifier.padding(padding)
        ) {
            composable("player") { PlayerScreen(playerVm) { navController.navigate("library") } }
            composable("library") { LibraryScreen(viewModel()) { song -> 
                playerVm.loadAndPlay(song)
                navController.navigate("player")
            } }
            // ... EQ and Settings routes
        }
    }
}
