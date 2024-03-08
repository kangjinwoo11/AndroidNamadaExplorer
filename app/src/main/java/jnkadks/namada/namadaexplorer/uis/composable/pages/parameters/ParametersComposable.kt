package jnkadks.namada.namadaexplorer.uis.composable.pages.parameters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import jnkadks.namada.namadaexplorer.common.Common
import jnkadks.namada.namadaexplorer.uis.composable.components.CardVertical
import jnkadks.namada.namadaexplorer.uis.composable.components.CardVerticalData
import jnkadks.namada.namadaexplorer.uis.composable.components.DataStateComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ErrorView
import jnkadks.namada.namadaexplorer.uis.composable.components.NavigationComposable
import jnkadks.namada.namadaexplorer.uis.composable.components.ProgressView

@Composable
fun ParametersComposable(
    navController: NavController,
    viewModel: ParametersViewModel = hiltViewModel()
) {
    NavigationComposable(
        navController = navController,
        title = "Parameters"
    ) {

        DataStateComposable(
            dataState = viewModel.parameterState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start,
            contentPadding = PaddingValues(vertical = 16.dp),
            onReload = {
                viewModel.loadParameters()
            },
            loading = {
                ProgressView()
            },
            error = { e ->
                ErrorView(error = e)
            },
        ) { parameters ->
            parametersComposable(
                title = "Chain Parameters",
                parameterDisplays = listOf(
                    ParameterDisplay(
                        title = "Max Tx Bytes",
                        Common.formattedWithCommas(parameters.parameters.maxTxBytes ?: 0)
                    ),
                    ParameterDisplay(
                        title = "Native Token",
                        "NAAN"
                    ),
                    ParameterDisplay(
                        title = "Min Num Of Blocks",
                        Common.formattedWithCommas(parameters.parameters.minNumOfBlocks ?: 0)
                    ),
                    ParameterDisplay(
                        title = "Max Expected Time Per Block",
                        Common.formattedWithCommas(
                            parameters.parameters.maxExpectedTimePerBlock ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Max Proposal Bytes",
                        Common.formattedWithCommas(
                            parameters.parameters.maxProposalBytes ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Epochs Per Year",
                        Common.formattedWithCommas(
                            parameters.parameters.epochsPerYear ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Max Signatures Per Transaction",
                        Common.formattedWithCommas(
                            parameters.parameters.maxSignaturesPerTransaction ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Max Block Gas",
                        Common.formattedWithCommas(
                            parameters.parameters.maxBlockGas ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Fee Unshielding Gas Limit",
                        Common.formattedWithCommas(
                            parameters.parameters.feeUnshieldingGasLimit ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Minimum Gas Price",
                        parameters.minimumGasPrice.naan
                    ),
                )
            )
            parametersComposable(
                title = "Proof of Stake Parameters",
                parameterDisplays = listOf(
                    ParameterDisplay(
                        title = "Max Validator Slots",
                        Common.formattedWithCommas(
                            parameters.posParams.maxValidatorSlots ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Pipeline Length",
                        Common.formattedWithCommas(
                            parameters.posParams.pipelineLen ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Unbonding Length",
                        Common.formattedWithCommas(
                            parameters.posParams.unbondingLen ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "TM Votes Per Token",
                        parameters.posParams.tmVotesPerToken ?: ""
                    ),
                    ParameterDisplay(
                        title = "Block Proposer Reward",
                        parameters.posParams.blockProposerReward ?: ""
                    ),
                    ParameterDisplay(
                        title = "Block Vote Reward",
                        parameters.posParams.blockVoteReward ?: ""
                    ),
                    ParameterDisplay(
                        title = "Max Inflation Rate",
                        parameters.posParams.maxInflationRate ?: ""
                    ),
                    ParameterDisplay(
                        title = "Target Staked Ratio",
                        parameters.posParams.targetStakedRatio ?: ""
                    ),
                    ParameterDisplay(
                        title = "Duplicate Vote Min Slash Rate",
                        parameters.posParams.duplicateVoteMinSlashRate ?: ""
                    ),
                    ParameterDisplay(
                        title = "Light Client Attack Min Slash Rate",
                        parameters.posParams.lightClientAttackMinSlashRate ?: ""
                    ),
                    ParameterDisplay(
                        title = "Cubic Slashing Window Length",
                        Common.formattedWithCommas(
                            parameters.posParams.cubicSlashingWindowLength ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Validator Stake Threshold",
                        parameters.posParams.validatorStakeThreshold ?: ""
                    ),
                    ParameterDisplay(
                        title = "Liveness Window Check",
                        Common.formattedWithCommas(
                            parameters.posParams.livenessWindowCheck ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Liveness Threshold",
                        parameters.posParams.livenessThreshold ?: ""
                    ),
                    ParameterDisplay(
                        title = "Rewards Gain P",
                        parameters.posParams.rewardsGainP ?: ""
                    ),
                    ParameterDisplay(
                        title = "Rewards Gain D",
                        parameters.posParams.rewardsGainD ?: ""
                    )
                )
            )
            parametersComposable(
                title = "Chain Parameters",
                parameterDisplays = listOf(
                    ParameterDisplay(
                        title = "Min Proposal Fund",
                        Common.formattedWithCommas(
                            parameters.govParams.minProposalFund ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Max Proposal Code Size",
                        Common.formattedWithCommas(
                            parameters.govParams.maxProposalCodeSize ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Min Proposal Voting Period",
                        Common.formattedWithCommas(
                            parameters.govParams.minProposalVotingPeriod ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Max Proposal Period",
                        Common.formattedWithCommas(
                            parameters.govParams.maxProposalPeriod ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Max Proposal Content Size",
                        Common.formattedWithCommas(
                            parameters.govParams.maxProposalContentSize ?: 0
                        )
                    ),
                    ParameterDisplay(
                        title = "Min Proposal Grace Epochs",
                        Common.formattedWithCommas(
                            parameters.govParams.minProposalGraceEpochs ?: 0
                        )
                    )
                )
            )
        }
    }
}

data class ParameterDisplay(
    val title: String,
    val value: String
)

private fun LazyListScope.parametersComposable(
    title: String,
    parameterDisplays: List<ParameterDisplay>
) {
    item {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            CardVertical(data = listOf(CardVerticalData.Title(text = title)))
        }
    }

    item {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            items(parameterDisplays) { display ->
                CardVertical(
                    modifier = Modifier.width(300.dp),
                    data = listOf(
                        CardVerticalData.SubTitle(text = display.title, maxLines = 2),
                        CardVerticalData.Title(text = display.value, maxLines = 1)
                    )
                )
            }
        }
    }
}