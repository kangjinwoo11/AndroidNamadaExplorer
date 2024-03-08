package jnkadks.namada.namadaexplorer.uis.composable.pages.proposal_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.models.ListProposals
import jnkadks.namada.namadaexplorer.uis.composable.components.BadgeComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.NavigationComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ProgressBarConfig
import jnkadks.namada.namadaexplorer.uis.composable.components.ProgressBarMultipleValueComposable

@Composable
fun ProposalDetailsComposable(
    navController: NavController,
    proposal: ListProposals.Proposal
) {
    NavigationComposable(
        navController = navController,
        title = "#${proposal.id}",
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(all = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier,
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = when (proposal.resultValue) {
                                ListProposals.Proposal.Result.Pending -> Color.Yellow
                                ListProposals.Proposal.Result.VotingPeriod -> Color.Green
                                ListProposals.Proposal.Result.Rejected -> MaterialTheme.colorScheme.errorContainer
                                else -> MaterialTheme.colorScheme.secondaryContainer
                            }
                        )
                    ) {
                        BadgeComposable(
                            title = proposal.resultValue?.value ?: proposal.result,
                            backgroundColor = Color.Transparent,
                            textColor = when (proposal.resultValue) {
                                ListProposals.Proposal.Result.Pending -> Color.Black
                                ListProposals.Proposal.Result.VotingPeriod -> MaterialTheme.colorScheme.scrim
                                ListProposals.Proposal.Result.Rejected -> MaterialTheme.colorScheme.error
                                else -> Color.Black
                            }
                        )
                    }

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
                        Box(
                            modifier = Modifier
                                .padding(vertical = 2.dp, horizontal = 4.dp)
                        ) {
                            Text(
                                text = proposal.kind,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

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
                        Text(text = "Author:", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = proposal.author.account,
                            style = MaterialTheme.typography.titleSmall
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Epoch (Start/End):",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.width(150.dp)
                            )
                            Text(
                                text = "${Common.formattedWithCommas(proposal.startEpoch)}/${
                                    Common.formattedWithCommas(
                                        proposal.endEpoch
                                    )
                                }",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Grade epoch:",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.width(150.dp)
                            )
                            Text(
                                text = Common.formattedWithCommas(proposal.graceEpoch),
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Votes:", style = MaterialTheme.typography.bodySmall)

                        var progressModifier: Modifier = Modifier
                            .clip(shape = RoundedCornerShape(7.dp))
                            .height(17.dp)

                        if (proposal.resultValue == ListProposals.Proposal.Result.Pending) {
                            progressModifier = progressModifier then Modifier.border(
                                width = 1.dp,
                                shape = RoundedCornerShape(7.dp),
                                color = Color.Black
                            )
                        }
                        Box(
                            modifier = progressModifier
                        ) {
                            if (proposal.resultValue == ListProposals.Proposal.Result.Pending) {
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
                                            value = proposal.yayVotes.toLong(),
                                            backgroundColor = Color.Green,
                                            textColor = Color.Black,
                                        ),
                                        ProgressBarConfig(
                                            value = proposal.nayVotes.toLong(),
                                            backgroundColor = Color.Red,
                                            textColor = Color.White,
                                        ),
                                        ProgressBarConfig(
                                            value = proposal.abstainVotes.toLong(),
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
}