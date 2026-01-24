package evolutionmod.cardsV1;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.cards.GlowingCard;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.orbsV1.*;

public class CrystalStone
		extends BaseEvoCard implements CustomSavable<Integer>, GlowingCard {
	public static final String cardID = "CrystalStone";
	public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalStone.png";
	private static final int COST = 2;
	private static final int BLOCK_AMT = 11;
	private static final int UPGRADE_BLOCK_AMT = 3;
	private static final int FORM_BLOCK = 3;
	private static final int UPGRADE_FORM_BLOCK = 1;

	private int genesIndexes;
	private AbstractGene firstGene; // firstIndex = genesIndex / 10
	private AbstractGene secondGene; // secoIndex = genesIndex % 10

	public CrystalStone() {
		this((!CardCrawlGame.isInARun() || AbstractDungeon.miscRng == null)
				? -1
				: AbstractDungeon.miscRng.random(11 * 10 - 1));
	}

	public CrystalStone(int geneIndexes) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, EvolutionEnum.EVOLUTION_BLUE,
				CardRarity.COMMON, CardTarget.SELF);
		this.block = this.baseBlock = BLOCK_AMT;
		this.magicNumber = this.baseMagicNumber = FORM_BLOCK;
		this.exhaust = true;
		this.genesIndexes = geneIndexes;
		resetGene();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int block = this.block;

		addToBot(new GainBlockAction(p, block));
		BaseEvoCard.formEffect(this.firstGene.ID);
		BaseEvoCard.formEffect(this.secondGene.ID, () -> addToBot(new RefundAction(this, 1)));
	}

	@Override
	protected void applyPowersToBlock() {
		this.calculateBlock();
		super.applyPowersToBlock();
		this.baseBlock = getBaseBlock();
		this.isBlockModified = this.block != this.baseBlock;
	}

	public int getBaseBlock() {
		return BLOCK_AMT + (this.upgraded ? UPGRADE_BLOCK_AMT : 0);
	}

	public void calculateBlock() {
		this.baseBlock = getBaseBlock();
		if (BaseEvoCard.isPlayerInThisForm(this.firstGene.ID)) {
			this.baseBlock += this.magicNumber;
		}
	}

	@Override
	public void onChoseThisOption() {
		addToBot(new MakeTempCardInHandAction(this, false));
	}

	@Override
	public AbstractCard makeCopy() {
		return this.secondGene == null ? new CrystalStone() : new CrystalStone(this.genesIndexes);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBlock(UPGRADE_BLOCK_AMT);
			this.upgradeMagicNumber(UPGRADE_FORM_BLOCK);
		}
	}

	@Override
	public int getNumberOfGlows() {
		return 2;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return firstGene != null && secondGene != null;
	}

	@Override
	public Color getGlowColor(int glowIndex) {
		switch (glowIndex) {
			case 0:
				if (isPlayerInThisForm(firstGene.ID)) {
					switch (firstGene.ID) {
						case HarpyGene.ID: return HarpyGene.COLOR.cpy();
						case MerfolkGene.ID: return MerfolkGene.COLOR.cpy();
						case LavafolkGene.ID: return LavafolkGene.COLOR.cpy();
						case CentaurGene.ID: return CentaurGene.COLOR.cpy();
						case LizardGene.ID: return LizardGene.COLOR.cpy();
						case BeastGene.ID: return BeastGene.COLOR.cpy();
						case PlantGene.ID: return PlantGene.COLOR.cpy();
						case ShadowGene.ID: return ShadowGene.COLOR.cpy();
						case LymeanGene.ID: return LymeanGene.COLOR.cpy();
						case InsectGene.ID: return InsectGene.COLOR.cpy();
						case SuccubusGene.ID: return SuccubusGene.COLOR.cpy();
					}
				}
				return AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
			case 1:
				if (isPlayerInThisForm(secondGene.ID, firstGene.ID)) {
					switch (secondGene.ID) {
						case HarpyGene.ID: return HarpyGene.COLOR.cpy();
						case MerfolkGene.ID: return MerfolkGene.COLOR.cpy();
						case LavafolkGene.ID: return LavafolkGene.COLOR.cpy();
						case CentaurGene.ID: return CentaurGene.COLOR.cpy();
						case LizardGene.ID: return LizardGene.COLOR.cpy();
						case BeastGene.ID: return BeastGene.COLOR.cpy();
						case PlantGene.ID: return PlantGene.COLOR.cpy();
						case ShadowGene.ID: return ShadowGene.COLOR.cpy();
						case LymeanGene.ID: return LymeanGene.COLOR.cpy();
						case InsectGene.ID: return InsectGene.COLOR.cpy();
						case SuccubusGene.ID: return SuccubusGene.COLOR.cpy();
					}
				}
				return AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
			default:
				return BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}

	private void resetGene() {
		if (this.genesIndexes < 0 || this.genesIndexes > 11 * 10 - 1) {
			return;
		}
		String[] validGenes = {
				PlantGene.ID,
				MerfolkGene.ID,
				HarpyGene.ID,
				LavafolkGene.ID,
				SuccubusGene.ID,
				LymeanGene.ID,
				InsectGene.ID,
				BeastGene.ID,
				LizardGene.ID,
				CentaurGene.ID,
				ShadowGene.ID};
		this.firstGene =  getGene(validGenes[this.genesIndexes / 11]);
		this.secondGene = getGene(validGenes[this.genesIndexes % 10 == this.genesIndexes / 11 ? 10 : this.genesIndexes % 10]);
		this.rawDescription = EXTENDED_DESCRIPTION[0]
				+ this.firstGene.ID + EXTENDED_DESCRIPTION[1]
				+ this.secondGene.ID + EXTENDED_DESCRIPTION[2];
		initializeDescription();
	}

	@Override
	public Integer onSave() {
		return this.genesIndexes;
	}

	@Override
	public void onLoad(Integer integer) {
		this.genesIndexes = integer;
		resetGene();
	}
}