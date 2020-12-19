package evolutionmod.cards;

import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.List;

public class CrystalStone
		extends BaseEvoCard implements CustomSavable<Integer> {
	public static final String ID = "evolutionmod:CrystalStone";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalStone.png";
	private static final int COST = 2;
	private static final int BLOCK_AMT = 10;
	private static final int UPGRADE_BLOCK_AMT = 3;
	private static final int FORM_BLOCK = 4;
	private static final int UPGRADE_FORM_BLOCK = 1;

	private int genesIndexes;
	private AbstractGene firstGene; // firstIndex = genesIndex / 10
	private AbstractGene secondGene; // secoIndex = genesIndex % 10

	public CrystalStone() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.COMMON, CardTarget.SELF);
		this.block = this.baseBlock = BLOCK_AMT;
		this.magicNumber = this.baseMagicNumber = FORM_BLOCK;
		this.exhaust = true;
		this.genesIndexes = -1;
		resetGene();
	}

	private CrystalStone(int geneIndexes) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
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

		BaseEvoCard.formEffect(this.firstGene.ID);
		addToBot(new GainBlockAction(p, block));
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
	public void triggerOnGlowCheck() {
		if (isPlayerInThisForm(secondGene.ID) && isPlayerInThisForm(firstGene.ID)) {
			this.glowColor = GOLD_BORDER_GLOW_COLOR.cpy();
		} else {
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		if (customTooltips == null) {
			super.getCustomTooltips();
			customTooltips.add(new TooltipInfo("Randomized forms",
					"The forms on this card are selected when the card is created and vary from a card to an other."));
		}
		return  customTooltips;
	}

	private void resetGene() {
		if (this.genesIndexes < 0) {
			if (!CardCrawlGame.isInARun() || AbstractDungeon.miscRng == null) {
				return;
			}
			this.genesIndexes = AbstractDungeon.miscRng.random(11 * 10 - 1);
		}
		AbstractGene[] validGenes = {
				new PlantGene(),
				new MerfolkGene(),
				new HarpyGene(),
				new LavafolkGene(),
				new SuccubusGene(),
				new LymeanGene(),
				new InsectGene(),
				new BeastGene(),
				new LizardGene(),
				new CentaurGene(),
				new ShadowGene()};
		this.firstGene =  validGenes[this.genesIndexes / 11];
		this.secondGene = validGenes[this.genesIndexes / 11 == this.genesIndexes % 10 ? 10 : this.genesIndexes % 10];
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