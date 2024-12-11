package com.example.medishareandroid.views.components

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medishareandroid.remote.Recommendation
import java.time.format.TextStyle

@Composable
fun RecommendationCard(recommendation: Recommendation,onClick: (() -> Unit)? = null){

    Card (modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 4.dp)
        .clickable(enabled = onClick != null) { onClick?.invoke() }
        .height(60.dp).fillMaxWidth(),
        elevation = 2.dp,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.White){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = recommendation.title,
                style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold,
                    fontSize = 16.sp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(recommendation.content,
                style = androidx.compose.ui.text.TextStyle(color = Color.Gray,
                    fontSize = 16.sp))
        }
    }


}
@Preview(showBackground = true)
@Composable
fun RecommendationCardPreviw(){

    RecommendationCard(Recommendation("title","content1","aaa","",""))
}
