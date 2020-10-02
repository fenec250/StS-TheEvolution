package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

public class LoyalCompanion
		extends BaseEvoCard implements CustomSavable<Integer> {
	public static final String ID = "evolutionmod:LoyalCompanion";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
	private static final int COST = 2;
	private static final int BLOCK_AMT = 10;
	private static final int UPGRADE_BLOCK_AMT = 3;
	private static final int DEX_AMT = 1;
	private static final int UPGRADE_DEX_AMT = 1;

	private int geneIndex;
	private AbstractGene gene;

	public LoyalCompanion() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION + EXTENDED_DESCRIPTION[0],
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.BASIC, CardTarget.SELF);
		this.block = this.baseBlock = BLOCK_AMT;
		this.magicNumber = this.baseMagicNumber = DEX_AMT;
		this.exhaust = true;
		this.geneIndex = -1;
		resetGene();
	}

	private LoyalCompanion(int geneIndex) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION + EXTENDED_DESCRIPTION[0],
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.BASIC, CardTarget.SELF);
		this.block = this.baseBlock = BLOCK_AMT;
		this.magicNumber = this.baseMagicNumber = DEX_AMT;
		this.exhaust = true;
		this.geneIndex = geneIndex;
		resetGene();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(new GainBlockAction(p, this.block));

		if (AbstractGene.isPlayerInThisForm(this.gene.ID)) {
			addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber)));
		} else {
			addToBot(new ChannelAction(this.gene.makeCopy()));
		}
	}


	@Override
	public AbstractCard makeCopy() {
		return this.gene == null ? new LoyalCompanion() : new LoyalCompanion(this.geneIndex);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBlock(UPGRADE_BLOCK_AMT);
			this.upgradeMagicNumber(UPGRADE_DEX_AMT);
		}
	}

	private void resetGene() {
		if (this.geneIndex < 0) {
			if (!CardCrawlGame.isInARun() || AbstractDungeon.miscRng == null) {
				return;
			}
			this.geneIndex = AbstractDungeon.miscRng.random(8 - 1);
		}
		AbstractGene[] validGenes = {
				new PlantGene(),
				new MerfolkGene(),
				new HarpyGene(),
				new LavafolkGene(),
				new SuccubusGene(),
				new LymeanGene(),
				new InsectGene(),
				new LizardGene()};
		this.gene = validGenes[this.geneIndex];
		this.rawDescription = DESCRIPTION + this.gene.getColoredName() + EXTENDED_DESCRIPTION[1];
		initializeDescription();
	}

	@Override
	public Integer onSave() {
		return this.geneIndex;
	}

	@Override
	public void onLoad(Integer integer) {
		this.geneIndex = integer;
		resetGene();
	}
}