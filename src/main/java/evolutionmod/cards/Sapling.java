package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.PlantGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.GrowthPower;

public class Sapling
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Sapling";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Barkskin.png";
    private static final int COST = 0;
    private static final int GROWTH_AMT = 1;
    private static final int FORM_GROWTH_AMT = 2;
    private static final int UPGRADE_FORM_GROWTH_AMT = 2;

    public Sapling() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = FORM_GROWTH_AMT;
        this.exhaust = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new GrowthPower(p, GROWTH_AMT)));
        if (isPlayerInThisForm(PlantGene.ID))
            this.exhaust = true;
        BaseEvoCard.formEffect(PlantGene.ID, () -> addToBot(new ApplyPowerAction(p, p, new GrowthPower(p, FORM_GROWTH_AMT))));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FORM_GROWTH_AMT);
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(PlantGene.ID)) {
            this.glowColor = PlantGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}