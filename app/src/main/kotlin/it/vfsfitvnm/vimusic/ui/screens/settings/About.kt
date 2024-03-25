package it.vfsfitvnm.vimusic.ui.screens.settings

//import it.vfsfitvnm.vimusic.BuildConfig

import android.content.pm.PackageManager
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import it.vfsfitvnm.vimusic.LocalPlayerAwareWindowInsets
import it.vfsfitvnm.vimusic.R
import it.vfsfitvnm.vimusic.enums.NavigationBarPosition
import it.vfsfitvnm.vimusic.ui.components.themed.HeaderWithIcon
import it.vfsfitvnm.vimusic.ui.styling.LocalAppearance
import it.vfsfitvnm.vimusic.utils.contentWidthKey
import it.vfsfitvnm.vimusic.utils.getVersionName
import it.vfsfitvnm.vimusic.utils.navigationBarPositionKey
import it.vfsfitvnm.vimusic.utils.preferences
import it.vfsfitvnm.vimusic.utils.rememberPreference
import it.vfsfitvnm.vimusic.utils.secondary


@ExperimentalAnimationApi
@Composable
fun About() {
    val (colorPalette, typography) = LocalAppearance.current
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val navigationBarPosition by rememberPreference(navigationBarPositionKey, NavigationBarPosition.Left)
    val contentWidth = context.preferences.getFloat(contentWidthKey,0.8f)

    Column(
        modifier = Modifier
            .background(colorPalette.background0)
            //.fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth(if (navigationBarPosition == NavigationBarPosition.Left) 1f else contentWidth)
            .verticalScroll(rememberScrollState())
            .padding(
                LocalPlayerAwareWindowInsets.current
                    .only(WindowInsetsSides.Vertical + WindowInsetsSides.End)
                    .asPaddingValues()
            )
    ) {
        HeaderWithIcon(
            title = stringResource(R.string.about),
            iconId = R.drawable.information,
            enabled = false,
            showIcon = true,
            modifier = Modifier,
            onClick = {}
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ) {
            BasicText(
                text = "RiMusic v${getVersionName()} By Arturo Cervantes 亗",
                style = typography.s.secondary,

                )
        }

        SettingsGroupSpacer()

        SettingsEntryGroupText(title = stringResource(R.string.social))

        SettingsEntry(
            title = "GitHub",
            text = stringResource(R.string.view_the_source_code),
            onClick = {
                uriHandler.openUri("https://github.com/Arturo254/RiMusic")
            }
        )

        SettingsGroupSpacer()

        SettingsEntryGroupText(title = "Mas + " )

        SettingsEntry(
            title = "Web Oficial",
            text = "Saldras de la aplicacion",
            onClick = {
                uriHandler.openUri("https://rimusic.netlify.app/")
            }
        )


        SettingsEntry(
            title = "Contacto",
            text = "(WhatsApp)",
            onClick = {
                uriHandler.openUri("https://wa.me/5576847925")
            }
        )

        SettingsGroupSpacer()

        SettingsEntryGroupText(title = stringResource(R.string.contributors))
        SettingsDescription(text = stringResource(R.string.in_alphabetical_order))

        SettingsTopDescription( text ="Translator:")
        SettingsTopDescription( text =
            "Arturo Cervantes Galindo 亗 "
        )

        SettingsTopDescription( text ="Developer / Designer:")
        SettingsTopDescription( text =
            "Arturo Cervantes Galindo 亗 "
        )
    }
}
