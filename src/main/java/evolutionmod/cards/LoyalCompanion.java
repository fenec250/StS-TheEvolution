package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
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

import java.util.ArrayList;
import java.util.List;

public class LoyalCompanion
		extends BaseEvoCard implements CustomSavable<Integer>, StartupCard {
	public static final String ID = "evolutionmod:LoyalCompanion";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalStone.png";
	private static final int COST = 2;
	private static final int BLOCK_AMT = 10;
	private static final int UPGRADE_BLOCK_AMT = 3;

	private int geneIndex;
	private AbstractGene gene;

	public LoyalCompanion() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION + EXTENDED_DESCRIPTION[0],
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.BASIC, CardTarget.SELF);
		this.block = this.baseBlock = BLOCK_AMT;
		this.magicNumber = this.baseMagicNumber = UPGRADE_BLOCK_AMT;
		this.exhaust = true;
		this.geneIndex = -1;
		resetGene();
	}

	private LoyalCompanion(int geneIndex) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION + EXTENDED_DESCRIPTION[0],
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.BASIC, CardTarget.SELF);
		this.block = this.baseBlock = BLOCK_AMT;
		this.magicNumber = this.baseMagicNumber = UPGRADE_BLOCK_AMT;
		this.exhaust = true;
		this.geneIndex = geneIndex;
		resetGene();
	}

	@Override
	public boolean atBattleStartPreDraw() {
		if (this.upgraded) {
			addToBot(new ChannelAction(this.gene.makeCopy()));
		}
		return false;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int block = this.block;

		if (!this.upgraded && AbstractGene.isPlayerInThisForm(this.gene.ID)) {
			block += this.magicNumber;
		} else if (!this.upgraded) {
			addToBot(new ChannelAction(this.gene.makeCopy()));
		}
		addToBot(new GainBlockAction(p, block));
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
			this.rawDescription = this.gene == null
					? DESCRIPTION + EXTENDED_DESCRIPTION[0] + EXTENDED_DESCRIPTION[2]
						+ EXTENDED_DESCRIPTION[3] + EXTENDED_DESCRIPTION[4]
					: DESCRIPTION + EXTENDED_DESCRIPTION[2] + this.gene.name + EXTENDED_DESCRIPTION[4];
			this.initializeDescription();
		}
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		if (customTooltips == null) {
			super.getCustomTooltips();
			customTooltips.add(new TooltipInfo("Randomized form",
					"The form on this card is selected when the run starts and varies from one run to another."));
		}
		return  customTooltips;
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