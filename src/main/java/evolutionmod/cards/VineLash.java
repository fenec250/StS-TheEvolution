package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import evolutionmod.actions.ThornDamageAction;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.LoseThornsPower;

public class VineLash
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:VineLash";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int THORN_AMT = 3;
    private static final int UPGRADE_THORN_AMT = 2;
    private static final int THORN_DAMAGE_AMT = 1;
//    private static final int UPGRADE_THORN_DAMAGE_AMT = 1;

    public VineLash() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = 0;
        this.magicNumber = this.baseMagicNumber = THORN_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToTop(
                new ApplyPowerAction(p, p, new ThornsPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToTop(
                new ApplyPowerAction(p, p, new LoseThornsPower(p, this.magicNumber), this.magicNumber));
        p.orbs.stream()
                .filter(o -> o instanceof PlantGene)
                .limit(1)
                .forEach(o -> this.addAdaptation(((AbstractGene) o).getAdaptation()));
        this.useAdaptations(p, m);
        AbstractDungeon.actionManager.addToBottom(new ThornDamageAction(p, m, THORN_DAMAGE_AMT));
    }

    @Override
    public AbstractCard makeCopy() {
        return new VineLash();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_THORN_AMT);
        }
    }
}