package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.LeafBirdAction;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.patches.AbstractCardEnum;

public class LeafBird
        extends BaseEvoCard implements GlowingCard {
    public static final String ID = "evolutionmod:LeafBird";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LeafBird.png";
    private static final int COST = 1;
    private static final int DISCARD_AMT = 2;
    private static final int UPGRADE_DISCARD_AMT = 1;
    private static final int PLANT_FORM_REFUND_AMT = 1;

    public LeafBird() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = DISCARD_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean harpy = isPlayerInThisForm(HarpyGene.ID);
//        boolean plant = isPlayerInThisForm(PlantGene.ID);
        addToBot(new LeafBirdAction(p, this.magicNumber, EXTENDED_DESCRIPTION[(harpy?1:0)] + this.magicNumber, harpy, false));
//        addToBot(new LeafBirdAction(p, this.magicNumber, EXTENDED_DESCRIPTION[(harpy?1:0)] + this.magicNumber, harpy, plant));
        formEffect(HarpyGene.ID);
        formEffect(PlantGene.ID, () -> addToBot(new RefundAction(this, PLANT_FORM_REFUND_AMT)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LeafBird();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DISCARD_AMT);
            this.initializeDescription();
        }
    }

	@Override
	public int getNumberOfGlows() {
		return 2;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return true;
	}

	@Override
	public Color getGlowColor(int glowIndex) {
		switch (glowIndex) {
			case 0:
				return isPlayerInThisForm(HarpyGene.ID) ? HarpyGene.COLOR.cpy()
						: BLUE_BORDER_GLOW_COLOR.cpy();
			case 1:
				return isPlayerInThisForm(PlantGene.ID, HarpyGene.ID) ? PlantGene.COLOR.cpy()
						: BLUE_BORDER_GLOW_COLOR.cpy();
			default:
				return BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}
}