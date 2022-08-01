package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.VenomGlandsPower;

public class VenomGlands2
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:VenomGlands";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/VenomGland.png";
    private static final int COST = 1;
    private static final int ENVENOM_AMT = 2;
    private static final int UPGRADE_ENVENOM_AMT = 1;

    public VenomGlands2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = ENVENOM_AMT;
        this.cardsToPreview = new Drone();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int poison = this.magicNumber;
		addToBot(new ApplyPowerAction(p, p, new VenomGlandsPower(p, poison)));
        formEffect(LizardGene.ID, () -> {
			addToBot(new MakeTempCardInHandAction(Drone.createDroneWithInteractions(p)));
        });
    }

    @Override
    public AbstractCard makeCopy() {
        return new VenomGlands2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_ENVENOM_AMT);
            initializeDescription();
        }
    }

	@Override
	public void triggerOnGlowCheck() {
		if (isPlayerInThisForm(LizardGene.ID)) {
			this.glowColor = LizardGene.COLOR.cpy();
		} else {
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}
}