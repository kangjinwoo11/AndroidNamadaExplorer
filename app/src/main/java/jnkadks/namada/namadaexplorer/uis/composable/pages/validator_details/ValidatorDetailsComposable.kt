package jnkadks.namada.namadaexplorer.uis.composable.pages.validator_details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.models.ListValidators
import jnkadks.namada.namadaexplorer.uis.composable.components.CardVertical
import jnkadks.namada.namadaexplorer.uis.composable.components.CardVerticalData
import jnkadks.namada.namadaexplorer.uis.composable.components.DataStateComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ErrorView
import jnkadks.namada.namadaexplorer.uis.composable.components.NavigationComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ProgressView
import jnkadks.namada.namadaexplorer.uis.composable.hook.OnStarted

@Composable
fun ValidatorDetailsComposable(
    navController: NavController,
    validator: ListValidators.CurrentValidator,
    viewModel: ValidatorDetailsViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    OnStarted(lifecycleState = lifecycleState) {
        viewModel.getBlockSignature(validator.address)
    }

    NavigationComposable(
        navController = navController,
        title = validator.moniker.ifBlank { "Validator details" }
    ) {
        DataStateComposable(
            dataState = viewModel.blockSignaturesState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(bottom = 52.dp, top = 16.dp)
                .padding(bottom = 16.dp),
            header = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Card(
                        modifier = Modifier,
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp, horizontal = 16.dp)
                        ) {
                            Text(text = "Address:", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = validator.address.uppercase(),
                                style = MaterialTheme.typography.titleSmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Voting power:", style = MaterialTheme.typography.bodySmall)
                            Text(
                                text = Common.formattedWithCommas(validator.votingPower),
                                style = MaterialTheme.typography.titleSmall
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tendermint Address:",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = validator.pubKey.value.uppercase(),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }

                    CardVertical(data = listOf(CardVerticalData.Title(text = "Signed blocks")))
                }
            },
            loading = {
                ProgressView()
            },
            error = { e ->
                ErrorView(error = e)
            },
        ) { blockSignatures ->
            LazyHorizontalGrid(
                rows = GridCells.Fixed(5),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(blockSignatures) { blockSignature ->
                    Card(
                        modifier = Modifier
                            .clickable { },
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (blockSignature.signStatus) {
                                Color.Green
                            } else {
                                MaterialTheme.colorScheme.errorContainer
                            }
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                        ) {
                            Text(
                                text = Common.formattedWithCommas(blockSignature.blockNumber),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = if (blockSignature.signStatus) {
                                        Color.Black
                                    } else {
                                        MaterialTheme.colorScheme.error
                                    }
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}