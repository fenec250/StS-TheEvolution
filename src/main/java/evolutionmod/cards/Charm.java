package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.LustPower;

public class Charm
        extends BaseEvoCard implements GlowingCard {
    public static final String ID = "evolutionmod:Charm";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/SuccubusSkl.png";
    private static final int COST = 1;
    private static final int REDUCTION_AMT = 3;
    private static final int UPGRADE_REDUCTION_AMT = 2;
    private static final int FORM_VULNERABLE = 2;
    private static final int UPGRADE_FORM_VULNERABLE = 2;

    public Charm() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = REDUCTION_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = this.magicNumber;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new LustPower(m, amount), amount));
        formEffect(SuccubusGene.ID, () -> addToBot(new ApplyPowerAction(m, p, new VulnerablePower(
                m, FORM_VULNERABLE + (upgraded ? UPGRADE_FORM_VULNERABLE : 0), false))));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Charm();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_REDUCTION_AMT);
        }
    }

    @Override
    public int getNumberOfGlows() {
        return 1;
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return isPlayerInThisForm(SuccubusGene.ID);
    }

    @Override
    public Color getGlowColor(int glowIndex) {
        return SuccubusGene.COLOR.cpy();
    }
}