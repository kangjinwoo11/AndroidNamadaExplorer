package jnkadks.namada.namadaexplorer.local

import android.content.Context
import com.moandjiezana.toml.Toml
import jnkadks.namada.namadaexplorer.R
import jnkadks.namada.namadaexplorer.models.Parameters

class TomlLocalImpl constructor(
    private val toml: Toml,
    private val context: Context,
) : TomlLocal {
    override suspend fun readParameters(): Parameters {
        toml.read(context.resources.openRawResource(R.raw.parameters))
        return Parameters(
            parameters = Parameters.Parameters(
                nativeToken = toml.getString("parameters.native_token"),
                minNumOfBlocks = toml.getLong("parameters.min_num_of_blocks"),
                maxExpectedTimePerBlock = toml.getLong("parameters.max_expected_time_per_block"),
                maxTxBytes = toml.getLong("parameters.max_tx_bytes"),
                maxProposalBytes = toml.getLong("parameters.max_proposal_bytes"),
                vpAllowlist = toml.getList("parameters.vp_allowlist"),
                txAllowlist = toml.getList("parameters.tx_allowlist"),
                implicitVp = toml.getString("parameters.implicit_vp"),
                epochsPerYear = toml.getLong("parameters.epochs_per_year"),
                maxSignaturesPerTransaction = toml.getLong("parameters.max_signatures_per_transaction"),
                maxBlockGas = toml.getLong("parameters.max_block_gas"),
                feeUnshieldingGasLimit = toml.getLong("parameters.fee_unshielding_gas_limit")
            ),
            posParams = Parameters.PosParams(
                maxValidatorSlots = toml.getLong("pos_params.max_validator_slots"),
                pipelineLen = toml.getLong("pos_params.pipeline_len"),
                unbondingLen = toml.getLong("pos_params.unbonding_len"),
                tmVotesPerToken = toml.getString("pos_params.tm_votes_per_token"),
                blockProposerReward = toml.getString("pos_params.block_proposer_reward"),
                blockVoteReward = toml.getString("pos_params.block_vote_reward"),
                maxInflationRate = toml.getString("pos_params.max_inflation_rate"),
                targetStakedRatio = toml.getString("pos_params.target_staked_ratio"),
                duplicateVoteMinSlashRate = toml.getString("pos_params.duplicate_vote_min_slash_rate"),
                lightClientAttackMinSlashRate = toml.getString("pos_params.light_client_attack_min_slash_rate"),
                cubicSlashingWindowLength = toml.getLong("pos_params.cubic_slashing_window_length"),
                validatorStakeThreshold = toml.getString("pos_params.validator_stake_threshold"),
                livenessWindowCheck = toml.getLong("pos_params.liveness_window_check"),
                livenessThreshold = toml.getString("pos_params.liveness_threshold"),
                rewardsGainP = toml.getString("pos_params.rewards_gain_p"),
                rewardsGainD = toml.getString("pos_params.rewards_gain_d")
            ),
            govParams = Parameters.GovParams(
                minProposalFund = toml.getLong("gov_params.min_proposal_fund"),
                maxProposalCodeSize = toml.getLong("gov_params.max_proposal_code_size"),
                minProposalVotingPeriod = toml.getLong("gov_params.min_proposal_voting_period"),
                maxProposalPeriod = toml.getLong("gov_params.max_proposal_period"),
                maxProposalContentSize = toml.getLong("gov_params.max_proposal_content_size"),
                minProposalGraceEpochs = toml.getLong("gov_params.min_proposal_grace_epochs")
            ),
            minimumGasPrice = Parameters.MinimumGasPriceParams(
                naan = toml.getString("parameters.minimum_gas_price.naan")
            )
        )
    }
}