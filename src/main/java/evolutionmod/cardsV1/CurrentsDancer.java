package evolutionmod.cardsV1;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.CurrentsDancerPower;
import evolutionmod.orbsV1.*;

public class CurrentsDancer
        extends BaseEvoCard {
    public static final String cardID = "CurrentsDancer";
    public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CurrentsDancer.png";
    private static final int COST = 1;
    private static final int DEXTERITY_AMT = 4;
    private static final int UPGRADE_DEXTERITY_AMT = 2;
    private static final int FORM_DRAW_AMT = 1;

    public CurrentsDancer() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, EvolutionEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = DEXTERITY_AMT;
//        this.block = this.baseBlock = DEXTERITY_AMT;
    }

    @Override
	public void use(AbstractPlayer p, AbstractMonster m) {
//		addToBot(new ApplyPowerAction(p, p,
//				new DexterityPower(p, this.magicNumber)));
//		BaseEvoCard.formEffect(HarpyGene.ID, () ->
//		{
//			if (!this.upgraded) {
//				addToBot(new ApplyPowerAction(p, p,
//						new DexterityPower(p, FORMS_DEXTERITY_AMT)));
//			} else {
//				BaseEvoCard.formEffect(MerfolkGene.ID, () ->
//						addToBot(new ApplyPowerAction(p, p,
//								new DexterityPower(p, FORMS_DEXTERITY_AMT))));
//			}
//		});
		addToBot(new ApplyPowerAction(p, p, new CurrentsDancerPower(p, this.magicNumber)));
		formEffect(MerfolkGene.ID, () -> addToBot(new DrawCardAction(FORM_DRAW_AMT)));
//		formEffect(MerfolkGene.ID, () -> addToBot(new GainBlockAction(p, this.block)));
	}

    @Override
    public AbstractCard makeCopy() {
        return new CurrentsDancer();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DEXTERITY_AMT);
//            this.upgradeBlock(UPGRADE_DEXTERITY_AMT);
//            this.rawDescription = UPGRADE_DESCRIPTION;
//            this.initializeDescription();
        }
    }

	@Override
	public void triggerOnGlowCheck() {
		if (isPlayerInThisForm(MerfolkGene.ID)) {
			this.glowColor = MerfolkGene.COLOR.cpy();
		}
	}
}