package com.example.medishareandroid.views.patient

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.medishareandroid.R
import com.example.medishareandroid.remote.BASE_URL
import com.example.medishareandroid.remote.RecReq
import com.example.medishareandroid.remote.Recommendation
import com.example.medishareandroid.remote.RecommendationApi
import com.example.medishareandroid.remote.ReqRes
import com.example.medishareandroid.remote.RetrofitInstance
import com.example.medishareandroid.repositories.PreferencesRepository
import retrofit2.Call

@Composable
fun ExactDesignScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9)) // Light background for the page
            .verticalScroll(rememberScrollState()),
    ) {
        // Blue header with user information
        BlueHeader()

        // Search bar positioned below the header
        SearchBar()

        // Clinics and Recommendations section
        ClinicsAndRecommendationsSection()

        Spacer(modifier = Modifier.height(15.dp))

        // Section title and list of cards
        SectionTitle("Clinics and Radiologists", navController)
        Spacer(modifier = Modifier.height(15.dp))

        ClinicsAndRadiologistsList()

        Spacer(modifier = Modifier.height(15.dp))

        SectionTitle("Recommendations", navController)
        Spacer(modifier = Modifier.height(15.dp))

        RecommendationsSection(navController)
        Spacer(modifier = Modifier.height(15.dp))

    }
}

@Composable
fun BlueHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(Color(0xC10A46F3)) // Blue header background
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image directly at the start with circular shape
            Image(
                painter = painterResource(id = R.drawable.hadh), // Replace with your image resource
                contentDescription = "MediShare Logo",
                modifier = Modifier
                    .size(80.dp) // Adjust the size of the image
                    .clip(CircleShape) // Clips the image into a circle

                    .border(
                        2.dp, Color.White, CircleShape
                    ) // Optional: Adds a border around the circular image
            )

            // Column for Text
            Column(
                modifier = Modifier
                    .weight(1f) // Ensures the text takes the available space
                    .padding(start = 16.dp) // Adds space between image and text
            ) {
                Text(
                    text = "Welcome to MediShare",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Your personalized dashboard", fontSize = 14.sp, color = Color.White
                )
            }
        }
    }
}


@Composable
fun SearchBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth() // Fill the width of the screen
            .offset(y = (-24).dp) // Floating effect

    ) {
        Row(
            modifier = Modifier
                .width(280.dp) // Fixed width for the search bar
                .height(45.dp)
                .align(Alignment.Center) // Center the Row horizontally within the Box

                .background(Color.White, RoundedCornerShape(16.dp)),
            verticalAlignment = Alignment.CenterVertically, // Center vertically
            horizontalArrangement = Arrangement.Center // Center horizontally within the Row
        ) {
            Spacer(modifier = Modifier.width(25.dp)) // This creates space before the icon

            Icon(
                imageVector = Icons.Default.Search, // Search icon from material icons
                contentDescription = "Search Icon",
                tint = Color.Gray,
                modifier = Modifier.size(15.dp) // Adjust the size of the icon
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "Search clinics or radiologists",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ClinicsAndRecommendationsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Clinics Card
        Box(
            modifier = Modifier
                .width(145.dp)
                .height(140.dp) // Set a fixed height for the Clinics card
                .background(Color(0x80AFECFE), RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.place), // Replace with your custom image
                    contentDescription = "ChatGPT Icon",
                    modifier = Modifier.size(20.dp) // Adjust size as needed
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Clinics and Radiologists",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Recommendations and Chat Cards Column
        Column(
            modifier = Modifier.weight(1f), // Adjust weight for the column
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Recommendations Card
            Box(
                modifier = Modifier
                    .fillMaxWidth() // Ensure Box fills the width of the screen
                    .height(65.dp) // Set a fixed height for the Recommendations card
                    .background(
                        Color(0x80BEB0FF), RoundedCornerShape(16.dp)
                    ), contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start, // Ensure the content is aligned at the start
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth() // Ensure the Row spans the full width of the Box
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.care), // Replace with your custom image
                        contentDescription = "Recommendations Icon",
                        modifier = Modifier.size(20.dp) // Adjust size as needed
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Recommendations",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chat Card
            Box(
                modifier = Modifier
                    .fillMaxWidth() // Ensure Box fills the width of the screen
                    .height(65.dp) // Set a fixed height for the Chat card
                    .background(
                        Color(0x80FFD6AE), RoundedCornerShape(16.dp)
                    ), contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start, // Ensure the content is aligned at the start
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth() // Ensure the Row spans the full width of the Box
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.chatgpt), // Replace with your custom image
                        contentDescription = "Chat Icon",
                        modifier = Modifier.size(20.dp) // Adjust size as needed
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Start new chat now",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, navController: NavController) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween, // Space between title and icon
        verticalAlignment = Alignment.CenterVertically // Align items vertically
    ) {
        Text(
            text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black
        )

        // Three-dot icon (More options)
        Icon(imageVector = Icons.Default.MoreVert, // Default three dots icon
            contentDescription = "See More",
            tint = Color.Black,// Set the color of the icon (you can use any color here)
            modifier = Modifier
                .size(24.dp) // Adjust the size of the icon
                .clickable {
                    navController.navigate("newPage")
                })
    }
}

@Composable
fun ClinicsAndRadiologistsList() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(5) { index ->
            // Pass actual data for each card
            RadiologistCard(
                name = "Radiologist $index",
                type = if (index % 2 == 0) "Clinic" else "Radiologist",
                openingHour = "8:00 AM",
                closingHour = "6:00 PM"
            )
        }
    }
}

@Composable
fun RecommendationsSection(navController: NavController) {
    //val viewModel = HomeViewModel()
    // val clinics = viewModel.clinics // Obtenir les données des cliniques
    val context = LocalContext.current
    val preferencesRepository = PreferencesRepository(context)

    // État pour les recommandations
    val recommendations = remember { mutableStateOf<List<Recommendation>>(emptyList()) }

    // Effectuer un appel réseau pour récupérer les recommandations
    LaunchedEffect(Unit) {
        val api = RetrofitInstance.getRetrofit().create(RecommendationApi::class.java)
        api.fetchRecommendations(RecReq(preferencesRepository.getId()!!))
            .enqueue(object : retrofit2.Callback<ReqRes> {
                override fun onResponse(call: Call<ReqRes>, response: retrofit2.Response<ReqRes>) {
                    if (response.isSuccessful) {
                        val recommendationsList = response.body()?.data
                        if (recommendationsList.isNullOrEmpty()) {
                            Toast.makeText(context, "No recommendations found", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            recommendations.value = recommendationsList
                        }
                    } else {
                        Toast.makeText(
                            context, "Failed to load recommendations", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ReqRes>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp) // Adds spacing between cards
    ) {
        // Each item inside the LazyRow will be a RecommendationsCard
        /*items(5) { // Adjust the number of items based on your data
            RecommendationsCard(
                title = "Improved Patient Care",
                description = "Innovative solutions for better healthcare."
            )

        }*/
        items(recommendations.value.size) { index ->
            RecommendationsCard(
                title = recommendations.value[index].title,
                description = recommendations.value[index].content,
                navController,
                recommendations.value[index].imageUrl
            )
        }


    }
}

@Composable
fun RadiologistCard(
    name: String, type: String, openingHour: String, closingHour: String
) {
    // Proper state management for favorite
    val isFavorite = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .width(250.dp)
            .height(135.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(10.dp)
    ) {
        Column {
            // Card Header (Title and Type)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black
                )
                IconButton(onClick = { isFavorite.value = !isFavorite.value }) {
                    Icon(
                        imageVector = if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite Toggle"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Type and Price
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = type,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            // Operating Hours
            Text(
                text = "Hours: $openingHour - $closingHour",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun RecommendationsCard(

    title: String, description: String, navController: NavController, image: String
) {
    // Card design with rounded corners, padding, and background color
    Box(modifier = Modifier
        .width(270.dp)
        .height(120.dp)
        .background(Color.White, RoundedCornerShape(16.dp))
        .padding(10.dp)
        .clickable {

            navController.navigate("recommendationItem/${title}/${description}/${image}")

        }) {
        Row {
            AsyncImage(
                model = BASE_URL + "upload/" + image,
                contentDescription = "Network Image",
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                    .padding(8.dp)
            )
            Column {
                // Card Header (Title and Description with Icon)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Title and Icon


                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                }

                Spacer(modifier = Modifier.height(6.dp))

                // Description below title
                Text(
                    text = description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewExactDesignScreen() {
    ExactDesignScreen(navController = rememberNavController())
}