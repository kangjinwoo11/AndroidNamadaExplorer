package jnkadks.namada.namadaexplorer.uis.composable.pages.validators

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.models.ListValidators
import jnkadks.namada.namadaexplorer.uis.composable.components.DataStateComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ErrorView
import jnkadks.namada.namadaexplorer.uis.composable.components.MiddleText
import jnkadks.namada.namadaexplorer.uis.composable.components.NavigationComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ProgressView

@Composable
fun ValidatorsComposable(
    navController: NavController,
    viewModel: ValidatorsViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationComposable(
        navController = navController,
        title = "Validators"
    ) {
        Surface {
            DataStateComposable(
                dataState = viewModel.validatorsState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 16.dp),
                onReload = {
                    viewModel.loadValidators()
                },
                loading = {
                    ProgressView()
                },
                empty = {

                },
                error = { e ->
                    ErrorView(error = e)
                },
            ) { index, item ->
                ValidatorComposable(
                    item = item,
                    modifier = Modifier.clickable {
                        navBackStackEntry?.savedStateHandle?.set(
                            "validator",
                            item
                        )
                        navController.navigate("validator_details")
                    }
                )
            }
        }
    }
}

@Composable
private fun ValidatorComposable(
    item: ListValidators.CurrentValidator,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Card(
            modifier = modifier,
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
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (item.moniker.trim().isBlank()) {
                    MiddleText(
                        text = item.address.uppercase(),
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                } else {
                    Text(
                        text = item.moniker,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = Common.formattedWithCommas(item.votingPower),
                        style = MaterialTheme.typography.bodySmall
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "${Common.formatDecimal(item.votingPercentage, 4)}%",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}