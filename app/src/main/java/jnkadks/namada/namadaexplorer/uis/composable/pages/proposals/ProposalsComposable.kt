package jnkadks.namada.namadaexplorer.uis.composable.pages.proposals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import jnkadks.namada.namadaexplorer.models.ListProposals
import jnkadks.namada.namadaexplorer.uis.composable.components.BadgeComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.DataStateComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ErrorView
import jnkadks.namada.namadaexplorer.uis.composable.components.NavigationComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ProgressBarConfig
import jnkadks.namada.namadaexplorer.uis.composable.components.ProgressBarMultipleValueComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ProgressView

@Composable
fun ProposalsComposable(
    navController: NavController,
    viewModel: ProposalsViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationComposable(
        navController = navController,
        title = "Proposals"
    ) {
        Surface {
            DataStateComposable(
                dataState = viewModel.proposalsState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                onReload = {
                    viewModel.loadProposals()
                },
                loading = {
                    ProgressView()
                },
                empty = {

                },
                error = { e ->
                    ErrorView(error = e)
                },
            ) { _, data ->
                ProposalComposable(
                    item = data,
                    modifier = Modifier
                        .clickable {
                            navBackStackEntry?.savedStateHandle?.set(
                                "proposal",
                                data
                            )
                            navController.navigate("proposal_details")
                        }
                )
            }
        }
    }
}

@Composable
private fun ProposalComposable(
    item: ListProposals.Proposal,
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BadgeComposable(
                        title = item.resultValue?.value ?: item.result,
                        backgroundColor = when (item.resultValue) {
                            ListProposals.Proposal.Result.Pending -> Color.Yellow
                            ListProposals.Proposal.Result.VotingPeriod -> Color.Green
                            ListProposals.Proposal.Result.Rejected -> MaterialTheme.colorScheme.errorContainer
                            else -> Color.Transparent
                        },
                        textColor = when (item.resultValue) {
                            ListProposals.Proposal.Result.Pending -> Color.Black
                            ListProposals.Proposal.Result.VotingPeriod -> MaterialTheme.colorScheme.scrim
                            ListProposals.Proposal.Result.Rejected -> MaterialTheme.colorScheme.error
                            else -> Color.Black
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = item.kind,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${item.id}     ".take(5),
                        maxLines = 1,
                        style = MaterialTheme.typography.titleSmall
                    )

                    var progressModifier: Modifier = Modifier
                        .clip(shape = RoundedCornerShape(7.dp))
                        .height(14.dp)

                    if (item.resultValue == ListProposals.Proposal.Result.Pending) {
                        progressModifier = progressModifier then Modifier.border(
                            width = 1.dp,
                            shape = RoundedCornerShape(7.dp),
                            color = Color.Black
                        )
                    }

                    Box(
                        modifier = progressModifier
                    ) {
                        if (item.resultValue == ListProposals.Proposal.Result.Pending) {
                            Text(
                                text = "Pending",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .background(Color.White)
                                    .fillMaxSize()
                            )
                        } else {
                            ProgressBarMultipleValueComposable(
                                values = listOf(
                                    ProgressBarConfig(
                                        value = item.yayVotes.toLong(),
                                        backgroundColor = Color.Green,
                                        textColor = Color.Black,
                                    ),
                                    ProgressBarConfig(
                                        value = item.nayVotes.toLong(),
                                        backgroundColor = Color.Red,
                                        textColor = Color.White,
                                    ),
                                    ProgressBarConfig(
                                        value = item.abstainVotes.toLong(),
                                        backgroundColor = Color.Gray,
                                        textColor = Color.Black,
                                    ),
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}