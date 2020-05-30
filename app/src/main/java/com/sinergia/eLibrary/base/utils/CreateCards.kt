package com.sinergia.eLibrary.base.utils

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import com.bumptech.glide.Glide
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.data.Model.Article
import com.sinergia.eLibrary.data.Model.Resource

class CreateCards {

    fun createResourceCard(context: Context, resource: Resource): CardView{

        // RESOURCE CARD
        var resourceCard = CardView(context)
        resourceCard.radius = 50f
        var cardParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = LinearLayout.LayoutParams.WRAP_CONTENT
            bottomMargin = 30
        }
        resourceCard.layoutParams = cardParams
        resourceCard.setBackgroundColor(getColor(context, R.color.colorGray))

        // CARD CONTENT
        var cardContent = LinearLayout(context)
        var contentParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
        cardContent.layoutParams = contentParams
        cardContent.orientation = LinearLayout.VERTICAL

        // CARD IMAGE
        var resourceImage = ImageView(context)
        var imageParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = 200
            bottomMargin = 10
            topMargin = 10
            rightMargin = 10
            leftMargin = 10
        }
        resourceImage.layoutParams = imageParams
        if(resource.imageUri != "noImage"){
            Glide
                .with(context)
                .load(Uri.parse(resource.imageUri))
                .fitCenter()
                .centerCrop()
                .into(resourceImage)
        } else {
            resourceImage.setImageResource(R.drawable.biblioteca)
        }


        // CARD SEPARATOR
        var separator = LinearLayout(context)
        var separatorParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = 5
            topMargin = 10
            bottomMargin = 10
        }
        separator.layoutParams = separatorParams
        separator.setBackgroundColor(getColor(context, R.color.colorLightGray))

        // CARD DESCRIPTION
        var resourceDescription = LinearLayout(context)
        var descriptionParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = LinearLayout.LayoutParams.WRAP_CONTENT

        }
        descriptionParams.setMargins(15,0,15,0)
        resourceDescription.layoutParams = descriptionParams
        resourceDescription.orientation = LinearLayout.VERTICAL

        val title = TextView(context)
        val titletxt = resource.title
        title.setText("Título: $titletxt.")
        title.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        resourceDescription.addView(title)

        val authors = TextView(context)
        var authorstxt = ""
        for(author in resource.author){
            authorstxt += "$author, "
        }
        authorstxt = authorstxt.substring(0, authorstxt.length-2)
        authors.setText("Autores: $authorstxt.")
        authors.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        resourceDescription.addView(authors)

        val isbn = TextView(context)
        val isbntxt = resource.isbn
        isbn.setText("ISBN: $isbntxt.")
        isbn.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        resourceDescription.addView(isbn)

        cardContent.addView(resourceImage)
        cardContent.addView(separator)
        cardContent.addView(resourceDescription)
        resourceCard.addView(cardContent)

        return resourceCard
    }

    fun createArticleCard(context: Context, article: Article): CardView{

        // ARTICLE CARD
        var articleCard = CardView(context)
        articleCard.radius = 50f
        var cardParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = LinearLayout.LayoutParams.WRAP_CONTENT
            bottomMargin = 30
        }
        articleCard.layoutParams = cardParams
        articleCard.setBackgroundColor(getColor(context, R.color.colorGray))

        // CARD CONTENT
        var cardContent = LinearLayout(context)
        var contentParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            gravity = Gravity.LEFT
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = LinearLayout.LayoutParams.WRAP_CONTENT
        }
        cardContent.layoutParams = contentParams
        cardContent.orientation = LinearLayout.HORIZONTAL

        // CARD IMAGE
        var articleImage = ImageView(context)
        var imageParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            width = 100
            height = 100
            bottomMargin = 10
            topMargin = 10
            rightMargin = 10
            leftMargin = 10
        }
        articleImage.layoutParams = imageParams
        articleImage.setImageResource(R.drawable.icon_pdf)

        // CARD DESCRIPTION
        var articleDescription = LinearLayout(context)
        var articleDescriptionParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            gravity = Gravity.CENTER_VERTICAL
            width = LinearLayout.LayoutParams.MATCH_PARENT
            height = LinearLayout.LayoutParams.WRAP_CONTENT

        }
        articleDescriptionParams.setMargins(15,0,15,0)
        articleDescription.layoutParams = articleDescriptionParams
        articleDescription.orientation = LinearLayout.VERTICAL

        val title = TextView(context)
        val titletxt = article.title
        title.setText("Título: $titletxt.")
        title.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        articleDescription.addView(title)

        val authors = TextView(context)
        var authorstxt = ""
        for(author in article.authors){
            authorstxt += "$author, "
        }
        authorstxt = authorstxt.substring(0, authorstxt.length-2)
        authorstxt += "."
        authors.setText("Autores: $authorstxt.")
        authors.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        articleDescription.addView(authors)

        val issn = TextView(context)
        val issntxt = article.issn
        issn.setText("Título: $issntxt.")
        issn.setTextColor(ContextCompat.getColor(context, R.color.colorWhite))
        articleDescription.addView(title)

        cardContent.addView(articleImage)
        cardContent.addView(articleDescription)
        articleCard.addView(cardContent)

        return articleCard

    }
}