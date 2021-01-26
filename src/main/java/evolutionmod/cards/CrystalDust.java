package evolutionmod.cards;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
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

public class CrystalDust
		extends BaseEvoCard implements CustomSavable<Integer>, StartupCard, GlowingCard {
	public static final String ID = "evolutionmod:CrystalDust";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalDust.png";
	private static final int COST = 0;
	private static final int COPIES_AMT = 1;
	private static final int UPGRADE_COPIES_AMT = 1;
	private static final int FORM_DRAW = 2;

	private int geneIndex;
	private AbstractGene gene;

	public CrystalDust() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = COPIES_AMT;
		this.exhaust = true;
		this.geneIndex = -1;
		resetGene();
	}

	private CrystalDust(int geneIndex) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = COPIES_AMT;
		this.exhaust = true;
		this.geneIndex = geneIndex;
		resetGene();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		BaseEvoCard.formEffect(this.gene.ID, () -> addToBot(new DrawCardAction(FORM_DRAW)));
	}

	@Override
	public boolean atBattleStartPreDraw() {
		addToBot(new MakeTempCardInDrawPileAction(this.makeStatEquivalentCopy(), this.magicNumber, true, true));
		return false;
	}

	@Override
	public void onChoseThisOption() {
		addToBot(new MakeTempCardInHandAction(this, false));
	}

	@Override
	public AbstractCard makeCopy() {
		return this.gene == null ? new CrystalDust() : new CrystalDust(this.geneIndex);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_COPIES_AMT);
			this.rawDescription = this.gene == null
					? UPGRADE_DESCRIPTION
					: this.gene.ID + EXTENDED_DESCRIPTION[1];
			this.initializeDescription();
		}
	}

	@Override
	public int getNumberOfGlows() {
		return 1;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return isPlayerInThisForm(gene.ID);
	}

	@Override
	public Color getGlowColor(int glowIndex) {
		switch (gene.ID) {
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
			default: return this.glowColor;
		}
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		if (customTooltips == null) {
			super.getCustomTooltips();
			customTooltips.add(new TooltipInfo("Randomized form",
					"The form on this card is selected when the card is created and vary from a card to an other."));
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
		this.gene = validGenes[this.geneIndex];
		this.rawDescription = this.gene.ID + (this.upgraded ? EXTENDED_DESCRIPTION[1] : EXTENDED_DESCRIPTION[0]);
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
//	@SpirePatch(clz = ShowCardAndAddToDiscardEffect.class, method = "update")
//	@SpirePatch(clz = ShowCardAndAddToHandEffect.class, method = "update")
//	@SpirePatch(clz = ShowCardAndAddToDrawPileEffect.class, method = "update")
//	public static class InDraw
//	{
//		public static void Prefix(AbstractGameEffect __instance)
//		{
//			if (__instance.duration == (float)ReflectionHacks.getPrivateStatic(__instance.getClass(), "EFFECT_DUR"))
//			{
////				jedi.onGenerateCardMidcombat((AbstractCard) ReflectionHacks.getPrivate(__instance, __instance.getClass(), "card"));
//			}
//		}
//	}
}