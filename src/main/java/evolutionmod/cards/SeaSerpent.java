package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import evolutionmod.orbs.LizardGene2;
import evolutionmod.patches.EvolutionEnum;

public class SeaSerpent
        extends BaseEvoCard implements GlowingCard {
    public static final String ID = "evolutionmodV2:SeaSerpent";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/SeaSerpent.png";
    private static final int COST = 2;
    private static final int BLOCK_AMT = 10;
    private static final int UPGRADE_BLOCK_AMT = 1;
    private static final int POISON_AMT = 2;

    public SeaSerpent() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);
        this.block = this.baseBlock = BLOCK_AMT;
        this.magicNumber = this.baseMagicNumber = POISON_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, this.block));
        boolean form = isPlayerInThisForm(LizardGene2.ID);
        if (form && this.upgraded)
            addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber));
        if (form || this.upgraded)
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        if (m.hasPower(PoisonPower.POWER_ID))
                            addToTop(new GainBlockAction(p, p, m.getPower(PoisonPower.POWER_ID).amount));
                        this.isDone = true;
                    }
                });
        formEffect(LizardGene2.ID);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(UPGRADE_BLOCK_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public int getNumberOfGlows() {
        return 1;
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return isPlayerInThisForm(LizardGene2.ID);
    }

    @Override
    public Color getGlowColor(int glowIndex) {
        return LizardGene2.COLOR.cpy();
    }
}