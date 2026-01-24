package evolutionmod.cards;

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
import evolutionmod.orbs.*;
import evolutionmod.patches.EvolutionEnum;

public class CrystalDust
		extends BaseEvoCard implements CustomSavable<Integer>, StartupCard {
	public static final String ID = "evolutionmodV2:CrystalDust";
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
	private String geneId;

	public CrystalDust() {
		this((!CardCrawlGame.isInARun() || AbstractDungeon.miscRng == null)
				? -1
				: AbstractDungeon.miscRng.random(GeneIds.length - 1));
	}

	public CrystalDust(int geneIndex) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = COPIES_AMT;
		this.exhaust = true;
		this.geneIndex = geneIndex;
		resetGene();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		BaseEvoCard.formEffect(this.geneId, () -> addToBot(new DrawCardAction(FORM_DRAW)));
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
		return this.geneId == null ? new CrystalDust() : new CrystalDust(this.geneIndex);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_COPIES_AMT);
			this.rawDescription = this.geneId == null
					? UPGRADE_DESCRIPTION
					: this.geneId + EXTENDED_DESCRIPTION[1];
			this.initializeDescription();
		}
	}


	@Override
	public void triggerOnGlowCheck() {
		if (geneId != null && isPlayerInThisForm(geneId)) {
			switch (geneId) {
				case HarpyGene2.ID: this.glowColor = HarpyGene2.COLOR.cpy(); return;
				case MerfolkGene2.ID: this.glowColor = MerfolkGene2.COLOR.cpy(); return;
				case LavafolkGene2.ID: this.glowColor = LavafolkGene2.COLOR.cpy(); return;
				case CentaurGene2.ID: this.glowColor = CentaurGene2.COLOR.cpy(); return;
				case LizardGene2.ID: this.glowColor = LizardGene2.COLOR.cpy(); return;
				case BeastGene2.ID: this.glowColor = BeastGene2.COLOR.cpy(); return;
				case PlantGene2.ID: this.glowColor = PlantGene2.COLOR.cpy(); return;
				case ShadowGene2.ID: this.glowColor = ShadowGene2.COLOR.cpy(); return;
				case LymeanGene2.ID: this.glowColor = LymeanGene2.COLOR.cpy(); return;
				case InsectGene2.ID: this.glowColor = InsectGene2.COLOR.cpy(); return;
				case SuccubusGene2.ID: this.glowColor = SuccubusGene2.COLOR.cpy(); return;
			}
		} else {
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}

	public void resetGene() {
		if (this.geneIndex < 0 || this.geneIndex > GeneIds.length - 1) {
			return;
		}
		this.geneId = GeneIds[this.geneIndex];
		this.rawDescription = this.geneId + (this.upgraded ? EXTENDED_DESCRIPTION[1] : EXTENDED_DESCRIPTION[0]);
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