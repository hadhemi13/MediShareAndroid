package com.example.medishareandroid.views.items


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medishareandroid.viewModels.patient.Clinic

@Composable
fun OCRCard(clinic: Clinic) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        //Row { Image(R.drawable.image1,
          //  "ocr")
        //}
    }
}

@Preview(showBackground = true)
@Composable
fun OCRCardPreview(){
    ClinicCard(clinic = Clinic("clinc","aaa"," 08 ","- 06PM"))
}
