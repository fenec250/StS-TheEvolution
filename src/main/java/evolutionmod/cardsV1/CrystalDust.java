package evolutionmod.cardsV1;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbsV1.LymeanGene;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.orbsV1.*;

public class CrystalDust
		extends BaseEvoCard implements CustomSavable<Integer>, StartupCard {
	public static final String cardID = "CrystalDust";
	public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
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
		this((!CardCrawlGame.isInARun() || AbstractDungeon.miscRng == null)
				? -1
				: AbstractDungeon.miscRng.random(11 - 1));
	}

	public CrystalDust(int geneIndex) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, EvolutionEnum.EVOLUTION_BLUE,
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
	public void triggerOnGlowCheck() {
		if (gene != null && isPlayerInThisForm(gene.ID)) {
			switch (gene.ID) {
				case HarpyGene.ID: this.glowColor = HarpyGene.COLOR.cpy(); return;
				case MerfolkGene.ID: this.glowColor = MerfolkGene.COLOR.cpy(); return;
				case LavafolkGene.ID: this.glowColor = LavafolkGene.COLOR.cpy(); return;
				case CentaurGene.ID: this.glowColor = CentaurGene.COLOR.cpy(); return;
				case LizardGene.ID: this.glowColor = LizardGene.COLOR.cpy(); return;
				case BeastGene.ID: this.glowColor = BeastGene.COLOR.cpy(); return;
				case PlantGene.ID: this.glowColor = PlantGene.COLOR.cpy(); return;
				case ShadowGene.ID: this.glowColor = ShadowGene.COLOR.cpy(); return;
				case LymeanGene.ID: this.glowColor = LymeanGene.COLOR.cpy(); return;
				case InsectGene.ID: this.glowColor = InsectGene.COLOR.cpy(); return;
				case SuccubusGene.ID: this.glowColor = SuccubusGene.COLOR.cpy(); return;
			}
		} else {
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}

	private void resetGene() {
		if (this.geneIndex < 0 || this.geneIndex > 11 - 1) {
			return;
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