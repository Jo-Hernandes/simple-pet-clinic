@file:OptIn(ExperimentalMaterial3Api::class)

package com.jonathas.petclinic.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonathas.petclinic.R
import com.jonathas.petclinic.model.PetItemModel
import com.jonathas.petclinic.ui.theme.Powder

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column {
            // buttons row
            Row(modifier = Modifier.height(100.dp)) {
                MainButton(
                    text = stringResource(id = R.string.chat),
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                if (1 == 1) {
                    MainButton(
                        text = stringResource(id = R.string.call),
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }

            // information
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "Office hours: 10 to 20",
                    Modifier
                        .padding(15.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
            ) {
                items(
                    count = 10,
                    itemContent = {
                        MainItem(PetItemModel("", "", "kakakka"))
                    })
            }
        }
    }
}

@Composable
fun MainButton(text: String, onClick: () -> Unit, modifier: Modifier) {
    Button(
        onClick = onClick, modifier.padding(10.dp), shape = RoundedCornerShape(15)
    ) {
        Text(
            text = text.uppercase(),
            color = Powder,
            modifier = Modifier.padding(15.dp),
            maxLines = 1,
            fontSize = 15.sp
        )
    }
}

@Composable
fun MainItem(item : PetItemModel) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 10.dp),

        elevation = CardDefaults.cardElevation(10.dp, pressedElevation = 3.dp),
        shape = RoundedCornerShape(15),
        onClick = {}
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = item.title)
            Text(text = item.title)
        }
    }

}