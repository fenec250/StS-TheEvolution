package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.PlantGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.GrowthPower;

public class Sapling
        extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Sapling";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/PlantSkl.png";
    private static final int COST = 0;
    private static final int GROWTH_AMT = 1;
    private static final int FORM_GROWTH_AMT = 3;
    private static final int UPGRADE_FORM_GROWTH_AMT = 2;

    public Sapling() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.COMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = FORM_GROWTH_AMT;
        this.exhaust = false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new GrowthPower(p, GROWTH_AMT)));
        if (isPlayerInThisForm(PlantGene2.ID))
            this.exhaust = true;
        BaseEvoCard.formEffect(PlantGene2.ID, () -> addToBot(new ApplyPowerAction(p, p, new GrowthPower(p, this.magicNumber))));
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
        if (isPlayerInThisForm(PlantGene2.ID)) {
            this.glowColor = PlantGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}