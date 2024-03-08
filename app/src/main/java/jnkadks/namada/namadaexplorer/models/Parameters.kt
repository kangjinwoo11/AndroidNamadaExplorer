package jnkadks.namada.namadaexplorer.models

data class Parameters(
    val parameters: Parameters,
    val posParams: PosParams,
    val govParams: GovParams,
    val minimumGasPrice: MinimumGasPriceParams
) {
    data class Parameters(
        val nativeToken: String,
        val minNumOfBlocks: Long?,
        val maxExpectedTimePerBlock: Long?,
        val maxTxBytes: Long?,
        val maxProposalBytes: Long?,
        val vpAllowlist: List<String>,
        val txAllowlist: List<String>,
        val implicitVp: String,
        val epochsPerYear: Long?,
        val maxSignaturesPerTransaction: Long?,
        val maxBlockGas: Long?,
        val feeUnshieldingGasLimit: Long?
    )

    data class PosParams(
        val maxValidatorSlots: Long?,
        val pipelineLen: Long?,
        val unbondingLen: Long?,
        val tmVotesPerToken: String?,
        val blockProposerReward: String?,
        val blockVoteReward: String?,
        val maxInflationRate: String?,
        val targetStakedRatio: String?,
        val duplicateVoteMinSlashRate: String?,
        val lightClientAttackMinSlashRate: String?,
        val cubicSlashingWindowLength: Long?,
        val validatorStakeThreshold: String?,
        val livenessWindowCheck: Long?,
        val livenessThreshold: String?,
        val rewardsGainP: String?,
        val rewardsGainD: String?
    )

    data class GovParams(
        val minProposalFund: Long?,
        val maxProposalCodeSize: Long?,
        val minProposalVotingPeriod: Long?,
        val maxProposalPeriod: Long?,
        val maxProposalContentSize: Long?,
        val minProposalGraceEpochs: Long?
    )

    data class MinimumGasPriceParams(
        val naan: String
    )
}