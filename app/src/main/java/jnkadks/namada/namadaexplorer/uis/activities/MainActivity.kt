package jnkadks.namada.namadaexplorer.uis.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jnkadks.namada.namadaexplorer.models.ListProposals
import jnkadks.namada.namadaexplorer.models.ListValidators
import jnkadks.namada.namadaexplorer.uis.composable.pages.block_details.BlockDetailsComposable
import jnkadks.namada.namadaexplorer.uis.composable.pages.block_details.BlockParameter
import jnkadks.namada.namadaexplorer.uis.composable.pages.blocks.BlocksComposable
import jnkadks.namada.namadaexplorer.uis.composable.pages.home.HomeComposable
import jnkadks.namada.namadaexplorer.uis.composable.pages.home.data.HomeState
import jnkadks.namada.namadaexplorer.uis.composable.pages.parameters.ParametersComposable
import jnkadks.namada.namadaexplorer.uis.composable.pages.proposal_details.ProposalDetailsComposable
import jnkadks.namada.namadaexplorer.uis.composable.pages.proposals.ProposalsComposable
import jnkadks.namada.namadaexplorer.uis.composable.pages.transaction_details.TransactionDetailsComposable
import jnkadks.namada.namadaexplorer.uis.composable.pages.transaction_details.TransactionParameter
import jnkadks.namada.namadaexplorer.uis.composable.pages.transactions.TransactionsComposable
import jnkadks.namada.namadaexplorer.uis.composable.pages.validator_details.ValidatorDetailsComposable
import jnkadks.namada.namadaexplorer.uis.composable.pages.validators.ValidatorsComposable
import jnkadks.namada.namadaexplorer.uis.theme.AndroidNamadaExplorerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContent {
            AndroidNamadaExplorerTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        HomeComposable(navController = navController)
                    }
                    for (item in HomeState.entries) {
                        composable(item.des) {
                            when (item) {
                                HomeState.VALIDATORS -> ValidatorsComposable(navController = navController)
                                HomeState.BLOCKS -> BlocksComposable(navController = navController)
                                HomeState.TRANSACTIONS -> TransactionsComposable(navController = navController)
                                HomeState.PROPOSALS -> ProposalsComposable(navController = navController)
                                HomeState.PARAMETERS -> ParametersComposable(navController = navController)
                            }
                        }
                    }
                    composable("validator_details") {
                        val validator =
                            navController.previousBackStackEntry?.savedStateHandle?.get<ListValidators.CurrentValidator>(
                                "validator"
                            )
                        if (validator != null) {
                            ValidatorDetailsComposable(
                                navController = navController,
                                validator = validator
                            )
                        }
                    }

                    composable("block_details") {
                        val parameter =
                            navController.previousBackStackEntry?.savedStateHandle?.get<BlockParameter>(
                                "block"
                            )
                        if (parameter != null) {
                            BlockDetailsComposable(
                                navController = navController,
                                parameter = parameter
                            )
                        }
                    }

                    composable("transaction_details") {
                        val parameter =
                            navController.previousBackStackEntry?.savedStateHandle?.get<TransactionParameter>(
                                "transaction"
                            )
                        if (parameter != null) {
                            TransactionDetailsComposable(
                                navController = navController,
                                parameter = parameter
                            )
                        }
                    }

                    composable("proposal_details") {
                        val proposal =
                            navController.previousBackStackEntry?.savedStateHandle?.get<ListProposals.Proposal>(
                                "proposal"
                            )
                        if (proposal != null) {
                            ProposalDetailsComposable(
                                navController = navController,
                                proposal = proposal
                            )
                        }
                    }
                }
            }
        }
    }
}