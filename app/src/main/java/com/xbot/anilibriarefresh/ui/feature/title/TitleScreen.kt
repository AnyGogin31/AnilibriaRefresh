package com.xbot.anilibriarefresh.ui.feature.title

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.xbot.anilibriarefresh.R
import com.xbot.anilibriarefresh.ui.components.ButtonComponent
import com.xbot.anilibriarefresh.ui.components.PosterImage
import com.xbot.anilibriarefresh.ui.components.TextEnding
import com.xbot.anilibriarefresh.ui.feature.title.components.BoxTitlePoster
import com.xbot.anilibriarefresh.ui.icons.AnilibriaIcons
import com.xbot.anilibriarefresh.ui.icons.Heart
import com.xbot.anilibriarefresh.ui.theme.FadeGradientColorStops
import com.xbot.anilibriarefresh.ui.theme.buttonPagerContentColorLightRed
import com.xbot.anilibriarefresh.ui.utils.only
import com.xbot.domain.model.DayOfWeek
import com.xbot.domain.model.EpisodeModel
import com.xbot.domain.model.MemberModel
import com.xbot.domain.model.PosterModel
import com.xbot.domain.model.TitleDetailModel
import kotlinx.coroutines.launch

@Composable
fun TitleScreen(
    modifier: Modifier = Modifier,
    viewModel: TitleViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val fadeGradientBrush = Brush.verticalGradient(colorStops = FadeGradientColorStops)

    Crossfade(targetState = state, label = "") { s ->
        when (s) {
            //TODO: Loading screen
            is TitleScreenState.Loading -> Surface(modifier = Modifier.fillMaxSize()) { }
            is TitleScreenState.Success -> {
                TitleScreenContent(
                    modifier = modifier,
                    title = s.title,
                    onClick = {},
                    paddingValues = paddingValues
                        .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom),
                    fadeGradient = fadeGradientBrush
                )
            }
        }
    }
}

@Composable
private fun TitleScreenContent(
    modifier: Modifier = Modifier,
    title: TitleDetailModel,
    onClick: () -> Unit,
    paddingValues: PaddingValues,
    fadeGradient: Brush,
) {
    val scrollState = rememberScrollState()
    val flowRowSize = remember { mutableIntStateOf(Int.MIN_VALUE) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(paddingValues = paddingValues)
    ) {

        posterTitle(
            title = title,
            fadeGradient = fadeGradient,
            scrollState = scrollState
        )
        shortInfoTitle(
            title = title,
            flowRowSize = flowRowSize
        )
        description(
            title = title,
            scrollState = scrollState,
            flowRowSize = flowRowSize
        )
        episodes()
        verticalItems(
            items = title.episodes
        ) { title ->
            if (title != null) {
                EpisodeItem(
                    item = title,
                    onClick = onClick
                )
            }
        }
    }
}

private fun LazyListScope.posterTitle(
    title: TitleDetailModel,
    fadeGradient: Brush,
    scrollState: ScrollState
) {
    item(
        contentType = { "ImageTitle" }
    ) {
        BoxTitlePoster(
            title = title,
            fadeGradient = fadeGradient,
            scrollState = scrollState
        )
    }
}

private fun LazyListScope.shortInfoTitle(
    title: TitleDetailModel,
    flowRowSize: MutableIntState
) {
    item(
        contentType = { "ShortInfoTitle" }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = title.name,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
        )
        TitleShortInfo(title = title, flowRowSize = flowRowSize)
        Spacer(Modifier.padding(10.dp))
        Text(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 4.dp),
            text = stringResource(R.string.description_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun LazyListScope.episodes() {
    item(
        contentType = { "EpisodeText" }
    ) {
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            modifier = Modifier
                .padding(start = 16.dp),
            text = stringResource(R.string.episodes_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun LazyListScope.verticalItems(
    items: List<EpisodeModel>,
    itemContent: @Composable LazyItemScope.(EpisodeModel?) -> Unit
) {
    items(
        count = items.size - 1
    ) {
        itemContent(items[it])
    }
}

private fun LazyListScope.description(
    title: TitleDetailModel,
    scrollState: ScrollState,
    flowRowSize: MutableIntState
) {
    item(
        contentType = { "DescriptionBox" }
    ) {
        DescriptionBox(
            text = title.description,
            scrollState = scrollState,
            flowRowSize = flowRowSize
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TitleShortInfo(
    modifier: Modifier = Modifier,
    title: TitleDetailModel,
    flowRowSize: MutableIntState
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${title.year}, "
            )
            Text(
                text = title.addedInUsersFavorites.toString()
            )
            Spacer(modifier = Modifier.padding(1.dp))
            Icon(
                imageVector = AnilibriaIcons.Filled.Heart,
                contentDescription = "",
                tint = buttonPagerContentColorLightRed,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.padding(5.dp))
        }
        Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            FlowRow(
                modifier = Modifier
                    .width(250.dp)
                    .onGloballyPositioned { coordinates ->
                        flowRowSize.intValue = coordinates.size.height
                    },
                horizontalArrangement = Arrangement.Center
            ) {
                title.genres.forEachIndexed { index, item ->
                    Text(
                        text = if (index != title.genres.size - 1) "$item, " else item
                    )
                }
            }
        }
        Row {
            TextEnding(episodes = title.episodesTotal ?: 0, str = ", ")
            Text(text = "${title.type}, ")
            Text(text = title.ageRating)
        }
    }
}

@Composable
private fun DescriptionBox(
    modifier: Modifier = Modifier,
    text: String,
    scrollState: ScrollState,
    flowRowSize: MutableIntState
) {
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    Column(modifier = modifier.padding(start = 16.dp, end = 16.dp)) {
        //TODO: переделать
//        if (expanded) {
//            LaunchedEffect(true) {
//                coroutineScope.launch {
//                    textLayoutResult.value?.let { layoutResult ->
//                        val scrollTo = layoutResult.size.height + flowRowSize.intValue
//                        scrollState.animateScrollTo(scrollTo)
//                    }
//                }
//            }
//        }
        Text(text = text,
            maxLines = if (expanded) Int.MAX_VALUE else 3,
            onTextLayout = { layoutResult ->
                textLayoutResult.value = layoutResult
            }
        )
        Spacer(modifier = Modifier.padding(4.dp))
        ButtonComponent(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    shape = RoundedCornerShape(8.dp),
                    color = Color.LightGray
                ),
            onClick = {
                expanded = !expanded
            },
            text = stringResource(
                if (expanded) R.string.hide_text_button else R.string.show_text_button
            ),
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EpisodeItem(
    modifier: Modifier = Modifier,
    item: EpisodeModel,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .width(400.dp)
            .padding(8.dp)
    ) {
        PosterImage(
            modifier = Modifier
                .height(100.dp)
                .width(150.dp)
                .clip(RoundedCornerShape(8.dp)),
            poster = item.preview
            )
        Spacer(modifier = Modifier.padding(5.dp))
        Text(
            text = "${item.ordinal}. ${item.name ?: "Серия ${item.ordinal}"}",
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )
    }
}

@Preview
@Composable
private fun TitleScreenPreview() {
    val titleModel = TitleDetailModel(
        id = 1,
        type = "ТВ",
        year = 2020,
        name = "Целитель, которого исключили из группы, оказался сильнейшим",
        season = "Лето",
        description = "Чтобы исекайнуться, иногда достаточно и простого перемещения во времени. Это, собственно, и происходит с Королём демонов Вельтором. Пав от руки героя пять столетий назад, он наконец-то возвращается к жизни, и готов вновь установить своё господство уже в новой эпохе. Однако это совсем не тот мир, который когда-то покорил Вельтор. За пять сотен лет смесь магии и технологий превратила средневековые ландшафты в самый настоящий киберпанк с огромными небоскрёбами, неоновыми вывесками и прочими прелестями жанра. Но амбиций Вельтора это не остановит. Пусть пока что наследие некогда могущественного демона сводится лишь к нескольким параграфам в учебниках истории, заблуждаться не стоит: очень скоро этот дивный новый мир будет снова у его ног. Чтобы исекайнуться, иногда достаточно и простого перемещения во времени. Это, собственно, и происходит с Королём демонов Вельтором. Пав от руки героя пять столетий назад, он наконец-то возвращается к жизни, и готов вновь установить своё господство уже в новой эпохе. Однако это совсем не тот мир, который когда-то покорил Вельтор. За пять сотен лет смесь магии и технологий превратила средневековые ландшафты в самый настоящий киберпанк с огромными небоскрёбами, неоновыми вывесками и прочими прелестями жанра.",
        poster = PosterModel(
            src = null,
            thumbnail = null
        ),
        freshAt = "2019-12-29T23:06:39+00:00",
        createdAt = "2019-12-29T23:06:39+00:00",
        updatedAt = "2023-08-20T15:08:20+00:00",
        isOngoing = false,
        ageRating = "16+",
        publishDay = DayOfWeek.SUNDAY,
        notification = "Серии выходят по воскресеньям",
        episodesTotal = 34,
        isInProduction = false,
        addedInUsersFavorites = 35034,
        averageDurationOfEpisode = 25,
        genres = listOf("Приключения", "Фантастика", "Экшен", "Романтика"),
        members = listOf(
            MemberModel(
                id = "e3d555b0",
                name = "Sharon",
                role = "Озвучка"
            ),
            MemberModel(
                id = "e3d55444",
                name = "HectoR",
                role = "Озвучка"
            ),
            MemberModel(
                id = "e3d55dddd",
                name = "Flerion",
                role = "Перевод и адаптация"
            ),
            MemberModel(
                id = "e221111dd",
                name = "Kiota",
                role = "Оформление"
            )
        ),
        episodes = listOf(
            EpisodeModel(
                id = "95c359d0",
                name = "Андерворлд",
                duration = 2880,
                preview = PosterModel(
                    src = null,
                    thumbnail = null
                ),
                hls480 = "",
                hls720 = "",
                hls1080 = "",
                ordinal = 1
            ),
            EpisodeModel(
                id = "95c359d1",
                name = "Древо зла",
                duration = 1440,
                preview = PosterModel(
                    src = null,
                    thumbnail = null
                ),
                hls480 = "",
                hls720 = "",
                hls1080 = "",
                ordinal = 2
            ),
            EpisodeModel(
                id = "95c322d1",
                name = "Пограничный Хребет",
                duration = 1550,
                preview = PosterModel(
                    src = null,
                    thumbnail = null
                ),
                hls480 = "",
                hls720 = "",
                hls1080 = "",
                ordinal = 3
            ),
            EpisodeModel(
                id = "951159d1",
                name = "Отправление",
                duration = 1980,
                preview = PosterModel(
                    src = null,
                    thumbnail = null
                ),
                hls480 = "",
                hls720 = "",
                hls1080 = "",
                ordinal = 4
            ),
            EpisodeModel(
                id = "9511444000",
                name = "Океаническая Черепаха",
                duration = 2120,
                preview = PosterModel(
                    src = null,
                    thumbnail = null
                ),
                hls480 = "",
                hls720 = "",
                hls1080 = "",
                ordinal = 5
            ),
        )
    )
    TitleScreenContent(title = titleModel, paddingValues = PaddingValues(0.dp), onClick = {}, fadeGradient = Brush.verticalGradient(colorStops = FadeGradientColorStops))
}