package evolutionmod.cards;

import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.*;
import evolutionmod.patches.EvolutionEnum;

import java.util.List;

public class LoyalCompanion
		extends BaseEvoCard implements CustomSavable<Integer>, StartupCard, GlowingCard {
	public static final String ID = "evolutionmodV2:LoyalCompanion";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/LoyalCompanion.png";
	private static final int COST = 2;
	private static final int BLOCK_AMT = 10;
	private static final int UPGRADE_BLOCK_AMT = 4;

	private int geneIndex;
	private AbstractGene gene;

	public LoyalCompanion() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
				CardRarity.BASIC, CardTarget.SELF);
		this.block = this.baseBlock = BLOCK_AMT;
		this.magicNumber = this.baseMagicNumber = UPGRADE_BLOCK_AMT;
		this.exhaust = true;
		this.geneIndex = -1;
		resetGene();
	}

	private LoyalCompanion(int geneIndex) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
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
			addToBot(((AbstractGene) this.gene.makeCopy()).getChannelAction());
			//this.gene.getAdaptation().apply(AbstractDungeon.player, null);
		}
		return false;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int block = this.block;
		if (!this.upgraded) {
			formEffect(this.gene.ID);
		}
		addToBot(new GainBlockAction(p, block));
	}

	@Override
	protected void applyPowersToBlock() {
		alterBlockAround(() -> super.applyPowersToBlock());
	}

	private void alterBlockAround(Runnable supercall) {
		this.baseBlock = BLOCK_AMT + (upgraded ? UPGRADE_BLOCK_AMT : 0);
		if (isPlayerInThisForm(this.gene.ID) && !upgraded) {
			this.baseBlock += this.magicNumber;
		}
		supercall.run();
		this.baseBlock = BLOCK_AMT + (upgraded ? UPGRADE_BLOCK_AMT : 0);
		this.isBlockModified = this.block != this.baseBlock;
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
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
			this.resetGene();
		}
	}

	@Override
	public int getNumberOfGlows() {
		return upgraded ? 0 : 1;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return isPlayerInThisForm(gene.ID);
	}

	@Override
	public Color getGlowColor(int glowIndex) {
		switch (gene.ID) {
			case HarpyGene2.ID: return HarpyGene2.COLOR.cpy();
			case MerfolkGene2.ID: return MerfolkGene2.COLOR.cpy();
			case LavafolkGene2.ID: return LavafolkGene2.COLOR.cpy();
			case CentaurGene2.ID: return CentaurGene2.COLOR.cpy();
			case LizardGene2.ID: return LizardGene2.COLOR.cpy();
			case BeastGene2.ID: return BeastGene2.COLOR.cpy();
			case PlantGene2.ID: return PlantGene2.COLOR.cpy();
			case ShadowGene2.ID: return ShadowGene2.COLOR.cpy();
			case LymeanGene2.ID: return LymeanGene2.COLOR.cpy();
			case InsectGene2.ID: return InsectGene2.COLOR.cpy();
			case SuccubusGene2.ID: return SuccubusGene2.COLOR.cpy();
			default: return AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
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
			this.geneIndex = AbstractDungeon.miscRng.random(11 - 1);
		}
		AbstractGene[] validGenes = {
				new PlantGene2(),
				new MerfolkGene2(),
				new HarpyGene2(),
				new LavafolkGene2(),
				new SuccubusGene2(),
				new LymeanGene2(),
				new InsectGene2(),
				new ShadowGene2(),
				new LizardGene2(),
				new CentaurGene2(),
				new BeastGene2()};
		this.gene = validGenes[this.geneIndex];
		this.rawDescription = EXTENDED_DESCRIPTION[0] + (!upgraded
				? this.gene.ID + EXTENDED_DESCRIPTION[1]
				: EXTENDED_DESCRIPTION[2] + this.gene.ID + EXTENDED_DESCRIPTION[3]);
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