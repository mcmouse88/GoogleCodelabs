package com.mcmouse88.accessibility_in_jetpack_compose.ui.article

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Colors
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.Typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcmouse88.accessibility_in_jetpack_compose.data.posts.post3
import com.mcmouse88.accessibility_in_jetpack_compose.model.Markup
import com.mcmouse88.accessibility_in_jetpack_compose.model.MarkupType
import com.mcmouse88.accessibility_in_jetpack_compose.model.Metadata
import com.mcmouse88.accessibility_in_jetpack_compose.model.Paragraph
import com.mcmouse88.accessibility_in_jetpack_compose.model.ParagraphType
import com.mcmouse88.accessibility_in_jetpack_compose.model.Post
import com.mcmouse88.accessibility_in_jetpack_compose.ui.theme.AccessibilityInJetpackComposeTheme

private val defaultSpacerSize = 16.dp

@Composable
fun PostContent(
    post: Post,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = defaultSpacerSize)
    ) {
        item {
            Spacer(modifier = Modifier.height(defaultSpacerSize))
            PostHeaderImage(post)
        }

        item {
            Text(text = post.title, style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(8.dp))
        }

        post.subtitle?.let { subtitle ->
            item {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.body2,
                        lineHeight = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(defaultSpacerSize))
            }
        }

        item {
            PostMetaData(post.metadata)
            Spacer(modifier = Modifier.height(24.dp))
        }
        items(post.paragraphs) {
            Paragraph(paragraph = it)
        }
        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun PostHeaderImage(
    post: Post
) {
    val imageModifier = Modifier
        .heightIn(min = 180.dp)
        .fillMaxWidth()
        .clip(shape = MaterialTheme.shapes.medium)

    Image(
        painter = painterResource(post.imageId),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = imageModifier
    )

    Spacer(modifier = Modifier.height(defaultSpacerSize))
}

@Composable
private fun PostMetaData(
    metadata: Metadata
) {
    val typography = MaterialTheme.typography
    Row(
        modifier = Modifier.semantics(mergeDescendants = true) {  }
    ) {
        Image(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            colorFilter = ColorFilter.tint(LocalContentColor.current),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = metadata.author.name,
                style = typography.caption,
                modifier = Modifier.padding(top = 4.dp)
            )
            
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = "${metadata.date} • ${metadata.readTimeMinutes} min read",
                    style = typography.caption
                )
            }
        }
    }
}

@Composable
private fun Paragraph(
    paragraph: Paragraph
) {
    val (textStyle, paragraphStyle, trailingPadding) = paragraph.type.getTextAndParagraphStyle()

    val annotatedString = paragraphToAnnotatedString(
        paragraph = paragraph,
        typography = MaterialTheme.typography,
        codeBlockBackground = MaterialTheme.colors.codeBlockBackground
    )

    Box(
        modifier = Modifier.padding(bottom = trailingPadding)
    ) {
        when (paragraph.type) {
            ParagraphType.Header -> {
                Text(
                    text = annotatedString,
                    style = textStyle.merge(paragraphStyle),
                    modifier = Modifier
                        .padding(4.dp)
                        .semantics { heading() }
                )
            }
            ParagraphType.CodeBlock -> {
                CodeBlockParagraph(
                    text = annotatedString,
                    textStyle = textStyle,
                    paragraphStyle = paragraphStyle
                )
            }

            ParagraphType.Bullet -> {
                BulletParagraph(
                    text = annotatedString,
                    textStyle = textStyle,
                    paragraphStyle = paragraphStyle
                )
            }
            else -> {
                Text(
                    text = annotatedString,
                    style = textStyle,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
private fun CodeBlockParagraph(
    text: AnnotatedString,
    textStyle: TextStyle,
    paragraphStyle: ParagraphStyle
) {
    Surface(
        color = MaterialTheme.colors.codeBlockBackground,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            style = textStyle.merge(paragraphStyle),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun BulletParagraph(
    text: AnnotatedString,
    textStyle: TextStyle,
    paragraphStyle: ParagraphStyle
) {
    Row {
        with(LocalDensity.current) {
            // this box is acting as character, so it's sized with font scaling (sp)
            Box(
                modifier = Modifier
                    .size(8.sp.toDp(), 8.sp.toDp())
                    .alignBy {
                        // Add an aligment "baseline" 1sp below the bottom of the circle
                        9.sp.roundToPx()
                    }
                    .background(color = LocalContentColor.current, shape = CircleShape)
            )
        }

        Text(
            text = text,
            style = textStyle.merge(paragraphStyle),
            modifier = Modifier
                .weight(1f)
                .alignBy(FirstBaseline)
        )
    }
}

private data class ParagraphStyling(
    val textStyle: TextStyle,
    val paragraphStyle: ParagraphStyle,
    val trailingPadding: Dp
)

@Composable
private fun ParagraphType.getTextAndParagraphStyle(): ParagraphStyling {
    val typography = MaterialTheme.typography
    var textStyle: TextStyle = typography.body1
    var paragraphStyle = ParagraphStyle()
    var trailingPadding = 24.dp

    when (this) {
        ParagraphType.Title -> textStyle = typography.h4
        ParagraphType.Caption -> textStyle = typography.body1
        ParagraphType.Header -> {
            textStyle = typography.h5
            trailingPadding = 16.dp
        }
        ParagraphType.Subhead -> {
            textStyle = typography.h6
            trailingPadding = 16.dp
        }
        ParagraphType.Text -> {
            textStyle = typography.body1.copy(lineHeight = 28.sp)
            paragraphStyle = paragraphStyle.copy(lineHeight = 28.sp)
        }
        ParagraphType.CodeBlock -> {
            textStyle = typography.body1.copy(fontFamily = FontFamily.Monospace)
        }
        ParagraphType.Quote -> textStyle = typography.body1
        ParagraphType.Bullet -> {
            paragraphStyle = ParagraphStyle(textIndent = TextIndent(firstLine = 8.sp))
        }
    }

    return ParagraphStyling(
        textStyle = textStyle,
        paragraphStyle = paragraphStyle,
        trailingPadding = trailingPadding
    )
}

private fun paragraphToAnnotatedString(
    paragraph: Paragraph,
    typography: Typography,
    codeBlockBackground: Color
): AnnotatedString {
    val styles: List<AnnotatedString.Range<SpanStyle>> = paragraph.markups
        .map { it.toAnnotatedStringItem(typography, codeBlockBackground) }
    return AnnotatedString(text = paragraph.text, spanStyles = styles)
}

fun Markup.toAnnotatedStringItem(
    typography: Typography,
    codeBlockBackground: Color
): AnnotatedString.Range<SpanStyle> {
    return when (this.type) {
        MarkupType.Link -> {
            AnnotatedString.Range(
                item = typography.body1.copy(
                    textDecoration = TextDecoration.Underline
                ).toSpanStyle(),
                start = start,
                end = end
            )
        }
        MarkupType.Code -> {
            AnnotatedString.Range(
                item = typography.body1.copy(
                    background = codeBlockBackground,
                    fontFamily = FontFamily.Monospace
                ).toSpanStyle(),
                start = start,
                end = end
            )
        }
        MarkupType.Italic -> {
            AnnotatedString.Range(
                item = typography.body1.copy(
                    fontStyle = FontStyle.Italic
                ).toSpanStyle(),
                start = start,
                end = end
            )
        }
        MarkupType.Bold -> {
            AnnotatedString.Range(
                item = typography.body1.copy(
                    fontWeight = FontWeight.Bold
                ).toSpanStyle(),
                start = start,
                end = end
            )
        }
    }
}

private val Colors.codeBlockBackground: Color
    get() = onSurface.copy(alpha = .15f)

@Preview("Post content")
@Preview("Post content (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPost() {
    AccessibilityInJetpackComposeTheme {
        Surface {
            PostContent(post = post3)
        }
    }
}